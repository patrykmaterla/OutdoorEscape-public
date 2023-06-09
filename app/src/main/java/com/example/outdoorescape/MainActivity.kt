package com.example.outdoorescape

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


const val REQUEST_CODE_SIGN_IN = 0

class MainActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest

    private lateinit var firebaseAuth: FirebaseAuth // auth



    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var TAG: String


    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var etEmailRegister: EditText
    private lateinit var etPasswordRegister: EditText
    private lateinit var tvLoggedIn: TextView
    private lateinit var etEmailLogin: EditText
    private lateinit var etPasswordLogin: EditText
    private lateinit var btnGoogleSignIn: SignInButton
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnSignOut: Button

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions


    override fun onStart() {
        super.onStart()
        var account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        etEmailRegister = findViewById(R.id.etEmailRegister)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        tvLoggedIn = findViewById(R.id.tvLoggedIn)
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        btnSignOut = findViewById(R.id.btnSignOut)

        gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mGoogleSignInClient.revokeAccess(); // Helps display the Google Sign In UI



//        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                result: ActivityResult ->
//            if (result.resultCode == REQUEST_CODE_SIGN_IN) {
//                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
//                account?.let {
//                    googleAuthForFirebase(it)
//                }
//            }
//        }


        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnSignOut.setOnClickListener {
            signOutUser()
        }

        btnGoogleSignIn.setOnClickListener {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, googleSignInOptions)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
//                activityResultLauncher.launch(it) // What is it?
            }
        }



//        findViewById(R.id.btnGoogleSignIn).setOnClickListener(this)
//
//        onClick(v: View) {
//
//        }













    // ---

        // Pobierz konto z wyniku logowania Google One Tap
        // val signInAccount = GoogleSignIn.getSignedInAccountFromIntent(data).result

// Pobierz idToken z konta Google
        // val idToken = signInAccount?.idToken

        // val googleAuthProvider = GoogleAuthProvider.getCredential(idToken, null)

//        oneTapClient = Identity.getSignInClient(this)

        /*
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
         */
/*
        // Configure the One Tap client for sign up
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
*/

        // Display the OneTap sign-in UI (if user doesn't have account do nothing i guess)
        /*
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                    Toast.makeText(this, "addonscuesfull work", Toast.LENGTH_LONG).show()

                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    Toast.makeText(this, "catch", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
                Toast.makeText(this, "FailFAIL", Toast.LENGTH_LONG).show()

            }
         */


        // Display the One Tap sign-up UI
/*
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0)

                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                Log.d(TAG, "AddOnFailureListener: ${e.localizedMessage}")
//                Toast.makeText(this, "signupFAIL", Toast.LENGTH_LONG).show()

            }
*/

    }

    /**
     * Sign out the user.
     */
    private fun signOutUser() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            mGoogleSignInClient.revokeAccess(); // Helps display the Google Sign In UI
            checkLoggedInState()
        }
    }

    private fun registerUser() {
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        clearUserInput()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Clears user input from <EditText> element.
     */
    private fun clearUserInput() {
        etEmailRegister.text.clear()
        etPasswordRegister.text.clear()
        etEmailLogin.text.clear()
        etPasswordLogin.text.clear()
    }

    /**
     * Test of KDoc
     *
     * @author Patryk Materla
     * @param no yes
     */
    private fun loginUser() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        clearUserInput()
                        // redirect user to dashboard

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Check if any user is signed in and
     * change the text that indicates if there is any user signed in.
     */
    private fun checkLoggedInState() {
        if (firebaseAuth.currentUser == null) { // not logged in
            tvLoggedIn.text = "You are not logged in"
        } else {
            tvLoggedIn.text = "You are logged in!"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firebaseAuth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Successfully logged in", Toast.LENGTH_LONG).show()
                    checkLoggedInState()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    checkLoggedInState()
                }
            }
        }
    }

//    private fun updateProfile() {
//        val user firebaseAuth.currentUser
//    }




//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == REQUEST_CODE_SIGN_IN) {
//            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
//            account?.let {
//                googleAuthForFirebase(it)
//            }
//        }
//    }



    // Stop displaying the OneTap UI
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    // ...
//                } catch (e: ApiException) {
//                    when (e.statusCode) {
//                        CommonStatusCodes.CANCELED -> {
//                            Log.d(TAG, "One-tap dialog was closed.")
//                            // Don't re-prompt the user.
//                            showOneTapUI = false
//                        }
//                        CommonStatusCodes.NETWORK_ERROR -> {
//                            Log.d(TAG, "One-tap encountered a network error.")
//                            // Try again or just ignore.
//                        }
//                        else -> {
//                            Log.d(
//                                TAG, "Couldn't get credential from result." +
//                                        " (${e.localizedMessage})"
//                            )
//                        }
//                    }
//                }
//            }
//
//        }
//
//    }
}
