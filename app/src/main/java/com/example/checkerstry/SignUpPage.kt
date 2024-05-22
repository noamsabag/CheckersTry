package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.databinding.SignupPageBinding
import com.google.firebase.auth.AuthResult

class SignUpPage : ComponentActivity(), View.OnClickListener
{
    lateinit var etUserName: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignUp: Button
    lateinit var etEmail: EditText
    lateinit var binding: SignupPageBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = SignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etUserName = binding.etUsername
        etEmail = binding.etEmail
        etPassword = binding.etPassword
        btnSignUp = binding.btnSignUp

        btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?)
    {
        FirebaseAuthHelper.createUser(etUserName.text.toString(), etPassword.text.toString()).addOnCompleteListener {
            if (it.isSuccessful)
            {
                FirebaseUsersHelper.initUser(it.result.user!!.uid, etUserName.text.toString())
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
