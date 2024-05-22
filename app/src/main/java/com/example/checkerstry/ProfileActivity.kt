package com.example.checkerstry

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvUserName.text = "${UserData.userName}(${UserData.eloRanking})"
        binding.tvGamesPlayed.text = "Games Played: ${UserData.gamesPlayed}"
        binding.tvWinRate.text = "Win Rate: ${(UserData.gamesWon * 100) / UserData.gamesPlayed}%"

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, GameHistoryFragment())
            commit()
        }
    }

}