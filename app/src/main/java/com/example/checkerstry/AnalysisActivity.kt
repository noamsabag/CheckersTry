package com.example.checkerstry

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.AnalysisActivityViewModel
import com.example.checkerstry.classes.GameActivityViewModel
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameView
import com.example.checkerstry.classes.Player
import com.example.checkerstry.classes.RegularGame
import com.example.checkerstry.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity() {
    lateinit var gameView: GameView
    private lateinit var binding: ActivityAnalysisBinding
    private lateinit var gameId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gameId = intent.getStringExtra("GAME_ID")!!
        GameData.setGameId(gameId)
        val viewModel = ViewModelProvider(this).get(AnalysisActivityViewModel::class.java)
        val game = viewModel.game

        gameView = GameView(this, listOf(Player.Black, Player.White), 8, GameActivity.pics, game as RegularGame)
        viewModel.turn.observe(this, gameView)
        binding.llAnalysis.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.btnMoveBack.setOnClickListener{
            game.unDoMove(viewModel.originalMoves[viewModel.currentMove].copy())
            viewModel.currentMove--
        }

        binding.btnMoveForward.setOnClickListener{
            viewModel.currentMove++
            game.doMove(viewModel.originalMoves[viewModel.currentMove].copy())
        }


    }

}