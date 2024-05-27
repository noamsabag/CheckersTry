package com.example.checkerstry

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GAMES_PATH
import com.example.checkerstry.classes.GameState
import com.example.checkerstry.classes.USERS_PATH
import com.example.checkerstry.classes.UserData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginPage : AppCompatActivity(), View.OnClickListener
{
    lateinit var etUserName: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogIn: Button
    lateinit var btnSignUp: Button
    override fun onCreate(savedInstanceState: Bundle?)
    {

        val dbRef = Firebase.database.getReference(USERS_PATH)
        dbRef.child("1").child("userName").setValue("User Name 1")
        dbRef.child("2").child("userName").setValue("User Name 2")



        FirebaseUsersHelper.loadUser("1")
        FirebaseUsersHelper.loadGameHistory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        etUserName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener(this)
        btnLogIn.setOnClickListener(this)

        val intent1 = Intent(this, OnlinePlayersCounter::class.java)
        startService(intent1)

        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)

    }

    override fun onClick(v: View?)
    {
        if (v?.id == R.id.btnSignUp)
        {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }
        else if (v?.id == R.id.btnLogIn)
        {

            FirebaseAuthHelper.singIn(etUserName.text.toString(), etPassword.text.toString()).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    FirebaseUsersHelper.loadUser(it.result.user!!.uid)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Password or email are incorrect", Toast.LENGTH_LONG).show()
                    etPassword.text.clear()
                    etUserName.text.clear()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
