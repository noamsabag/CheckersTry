package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GameHistoryAdapter
import com.example.checkerstry.databinding.ActivityGameHistoryBinding

class GameHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gamesList = FirebaseUsersHelper.loadGameHistory()

        binding.listView.isClickable = true
        binding.listView.adapter = GameHistoryAdapter(this, gamesList)
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("GAME_ID", gamesList[position].gameId)
            startActivity(intent)
        }
    }

}