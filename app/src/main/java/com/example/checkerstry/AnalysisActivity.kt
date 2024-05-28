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
import com.example.checkerstry.databinding.ActivityAnalysisBinding

/**
 * Activity for analyzing past games, allowing users to step through each move made during the game.
 */
class AnalysisActivity : AppCompatActivity() {
    private lateinit var gameView: GameView  // Custom view to display the game board.
    private lateinit var binding: ActivityAnalysisBinding  // View binding for layout access.
    private lateinit var gameId: String  // Game ID for fetching specific game data.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the game ID passed from the previous activity.
        gameId = intent.getStringExtra("GAME_ID")!!
        GameData.gameId = gameId  // Set the global game ID.

        // Initialize the ViewModel and fetch the game data.
        val viewModel = ViewModelProvider(this).get(AnalysisActivityViewModel::class.java)
        val game = viewModel.game

        // Setup the game view and add it to the layout.
        gameView = GameView(this, listOf(Player.Black, Player.White), GameActivity.pics, game)
        viewModel.turn.observe(this, gameView)
        binding.llAnalysis.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        // Button to move back in the move history of the game.
        binding.btnMoveBack.setOnClickListener {
            if (viewModel.currentMove >= 0)
            {
                viewModel.moveBack()
            } else
            {
                Toast.makeText(this, "You Can't Move Back, This is the starting position", Toast.LENGTH_LONG).show()
            }
        }

        // Button to move forward in the move history of the game.
        binding.btnMoveForward.setOnClickListener {
            if (viewModel.currentMove < viewModel.originalMoves.size - 1)
            {
                viewModel.moveForward()
            } else
            {
                Toast.makeText(this, "You Can't Move Forward, This is the last position", Toast.LENGTH_LONG).show()
            }
        }
    }
}
