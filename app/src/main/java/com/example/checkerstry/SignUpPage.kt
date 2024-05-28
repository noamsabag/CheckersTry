package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.databinding.SignupPageBinding

/**
 * Activity for registering a new user. It handles user input for registration details,
 * communicates with Firebase to create an account, and navigates to the main activity upon successful registration.
 */
class SignUpPage : ComponentActivity(), View.OnClickListener {
    private lateinit var etUserName: EditText  // Input field for user's name
    private lateinit var etPassword: EditText  // Input field for password
    private lateinit var etEmail: EditText    // Input field for email
    private lateinit var btnSignUp: Button    // Button for submitting the registration
    private lateinit var binding: SignupPageBinding  // View binding for accessing the UI components

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind UI components
        etUserName = binding.etUsername
        etEmail = binding.etEmail
        etPassword = binding.etPassword
        btnSignUp = binding.btnSignUp

        // Set up the button click listener
        btnSignUp.setOnClickListener(this)
    }

    /**
     * Handles click events on the sign-up button. It attempts to create a new user account with the provided credentials.
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        if (v == btnSignUp) {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            FirebaseAuthHelper.createUser(email, password) { task ->
                if (task.isSuccessful) {
                    // Initialize user data in Firebase and navigate to the main activity
                    FirebaseUsersHelper.initUser(task.result.user!!.uid, etUserName.text.toString())
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Show error message and clear relevant fields if the registration fails
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    etPassword.text.clear()
                    etUserName.text.clear()
                }
            }
        }
    }
}
