package com.example.checkerstry

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.ActivityProfileBinding
import com.google.firebase.auth.auth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvUserName.text = "${UserData.userName}(${UserData.eloRanking})"
        binding.tvGamesPlayed.text = "Games Played: ${UserData.gamesPlayed}"
        val winRate = if (UserData.gamesPlayed != 0) (UserData.gamesWon * 100) / UserData.gamesPlayed else 0
        binding.tvWinRate.text = "Win Rate: $winRate%"

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, GameHistoryFragment())
            commit()
        }

        binding.btnDeleteAccount.setOnClickListener {
            deleteAccount()
        }
    }



    fun deleteAccount()
    {
        val dialog = ProgressDialog(this)
        dialog.setMessage("deleting account")
        dialog.show()
        FirebaseAuthHelper.deleteAccount {
            dialog.cancel()
            UserData.reset()
            intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}