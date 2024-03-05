package com.example.outdoorescape

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Fragment that let user choose to either: sign in or sign up to his account.
 * Temporarily using the app without an account is enabled via pressing `tvWithoutAnAccount`.
 */
class WelcomeFragment : Fragment() {

    private lateinit var tvWithoutAccount: TextView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    // Shared preference is used to save user sign in state
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity!!.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        // Continue without an account
        tvWithoutAccount = view.findViewById<TextView>(R.id.tvWithoutAccount)
        tvWithoutAccount.setOnClickListener {
            val editor = sharedPreferences.edit().apply() {
                putBoolean("is_signed_in", true)
                apply()
            }
            Intent(requireActivity(), MainActivity::class.java).apply {
                startActivity(this)
            }
        }
        // Go to SignInFragment
        btnSignIn = view.findViewById<Button>(R.id.btnSignIn)
        btnSignIn.setOnClickListener {
            (activity as AuthActivity?)?.setCurrentFragment(SignInFragment())
        }
        // Go to SignUpFragment
        btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            (activity as AuthActivity?)?.setCurrentFragment(SignUpFragment())
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(param1: String, param2: String) = WelcomeFragment()
    }
}