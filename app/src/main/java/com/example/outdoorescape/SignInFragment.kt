package com.example.outdoorescape

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shobhitpuri.custombuttons.GoogleSignInButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnBack: ImageButton
    private lateinit var btnSignIn: Button
    private lateinit var btnGoogleSignIn: GoogleSignInButton
    private lateinit var etEmailSignIn: EditText
    private lateinit var etPasswordSignIn: EditText
    // Shared preference is used to save user sign in state
    private lateinit var sharedPreferences: SharedPreferences

    private fun clearUserInput() {
        etEmailSignIn.text.clear()
        etPasswordSignIn.text.clear()
    }

    /**
     * Handle email + password authorization.
     */
    private fun signIn() {
        val email = etEmailSignIn.text.toString()
        val password = etPasswordSignIn.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Check if email and password is correct (if not throw exception)
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        clearUserInput()
                        // Go to MainActivity
                        val editor = sharedPreferences.edit().apply() {
                            putBoolean("is_signed_in", true)
                            apply()
                        }
                        // Set isLoggedIn flag
                        Intent(requireActivity(), MainActivity::class.java).apply {
                            startActivity(this)
                        }

                    }
                }
                catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Handle authorization using Google Sign In Button
     * googleSignIn(), onActivityResult(), googleAuthForFirebase()
     */
    private fun googleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        signInClient.signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGN_IN) // launch: onActivityResult()
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            // Handle successful authentication
            try {
                val authResult = firebaseAuth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Successfully signed in", Toast.LENGTH_LONG).show()
                    // Set isLoggedIn Flag
                    val editor = sharedPreferences.edit().apply() {
                        putBoolean("is_signed_in", true)
                        apply()
                    }
                    // Go to MainActivity
                    Intent(requireActivity(), MainActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.result?.let {
                    googleAuthForFirebase(it) // Handle result in separate function
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity!!.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnSignIn = view.findViewById(R.id.btnSignIn)
        btnGoogleSignIn = view.findViewById(R.id.btnGoogleSignIn)
        etEmailSignIn = view.findViewById(R.id.etEmailSignIn)
        etPasswordSignIn = view.findViewById(R.id.etPasswordSignIn)
        firebaseAuth = FirebaseAuth.getInstance()
        // Back button
        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        btnSignIn.setOnClickListener {
            signIn()
        }
        btnGoogleSignIn.setOnClickListener {
            googleSignIn()
        }
        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) = SignInFragment()
        const val REQUEST_CODE_SIGN_IN = 0
    }
}