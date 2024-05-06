package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.FinishedGameData
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GameHistoryAdapter
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.User
import com.example.checkerstry.databinding.ActivityGameHistoryBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class GameHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameHistoryBinding
    val gamesList: ArrayList<FinishedGameData> = FirebaseUsersHelper.gamesPlayed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //gamesList.add(FinishedGameData(User("1", 0, 0, 800), User("2", 0, 0, 800), GameType.RegularGame, "-Nx7FuNpkOU1tbBwYe7x"))

        binding.listView.isClickable = true
        binding.listView.adapter = GameHistoryAdapter(this, gamesList)
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, AnalysisActivity::class.java)
            intent.putExtra("GAME_ID", gamesList[position].gameId)
            startActivity(intent)
        }
    }

}