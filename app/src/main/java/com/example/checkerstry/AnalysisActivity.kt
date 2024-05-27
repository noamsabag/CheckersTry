package com.example.checkerstry

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.checkerstry.classes.AnalysisActivityViewModel
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameView
import com.example.checkerstry.classes.Player
import com.example.checkerstry.classes.Game
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
        GameData.gameId = gameId
        val viewModel = ViewModelProvider(this).get(AnalysisActivityViewModel::class.java)
        val game = viewModel.game

        gameView = GameView(this, listOf(Player.Black, Player.White), GameActivity.pics, game as Game)
        viewModel.turn.observe(this, gameView)
        binding.llAnalysis.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.btnMoveBack.setOnClickListener{
            if (viewModel.currentMove >= 0)
            {
                viewModel.moveBack()
            }
            else
            {
                Toast.makeText(this, "You Can't Move Back, This is the starting position", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnMoveForward.setOnClickListener{
            if (viewModel.currentMove < viewModel.originalMoves.size - 1)
            {
                viewModel.moveForward()
            }
            else
            {
                Toast.makeText(this, "You Can't Move Forward, This is the last position", Toast.LENGTH_LONG).show()

            }
        }


    }

}