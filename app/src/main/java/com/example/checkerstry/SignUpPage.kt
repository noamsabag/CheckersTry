package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.ui.theme.CheckersTryTheme
import com.google.firebase.auth.AuthResult

class SignUpPage : ComponentActivity(), View.OnClickListener
{
    lateinit var etUserName: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignUp: Button
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        etUserName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?)
    {
        FirebaseAuthHelper.createUser(etUserName.text.toString(), etPassword.text.toString()).addOnCompleteListener {
            if (it.isSuccessful)
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else
            {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                etPassword.text.clear()
                etUserName.text.clear()
            }
        }
    }
}
