package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.FirebaseUsersHelper

/**
 * Activity for user login, handling authentication and navigation to other activities.
 */
class LoginPage : AppCompatActivity(), View.OnClickListener {
    // Late-initialized properties for UI components
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Stopping the service if it was already started
        stopService(Intent(this, OnlinePlayersCounter::class.java))

        // Binding UI components with their respective IDs
        etUserName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUp = findViewById(R.id.btnSignUp)

        // Setting click listeners for the login and sign up buttons
        btnSignUp.setOnClickListener(this)
        btnLogIn.setOnClickListener(this)

        // Starting a background service to monitor online players
        val intent1 = Intent(this, OnlinePlayersCounter::class.java)
        startService(intent1)
    }

    /**
     * Handles click events on the login and sign up buttons.
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSignUp -> {
                // Navigate to the SignUpPage activity
                val intent = Intent(this, SignUpPage::class.java)
                startActivity(intent)
            }
            R.id.btnLogIn -> {
                // Attempt to sign in using entered credentials
                FirebaseAuthHelper.signIn(etUserName.text.toString(), etPassword.text.toString()) { task ->
                    if (task.isSuccessful) {
                        // Load user data and navigate to the main activity
                        FirebaseUsersHelper.loadUser(task.result.user!!.uid)
                        FirebaseUsersHelper.loadGameHistory()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Display an error and clear input fields if login fails
                        Toast.makeText(this, "Password or email are incorrect", Toast.LENGTH_LONG).show()
                        etPassword.text.clear()
                        etUserName.text.clear()
                    }
                }
            }
        }
    }

    /**
     * Overrides the back press to disable back navigation from the login screen.
     */
    override fun onBackPressed() {
        // Prevents back navigation
    }
}
