package com.example.checkerstry

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.checkerstry.classes.*

/**
 * Activity to host and display the game board for both online and offline games.
 * It includes timers, player information, and manages game state updates through a ViewModel.
 */
class GameActivity : ComponentActivity() {
    private val handler = Handler(Looper.getMainLooper()) // Handler to post runnable tasks on the main UI thread
    private lateinit var gameView: GameView // The view that displays the game board
    private lateinit var llLayout: LinearLayout // Layout to contain the game view
    private lateinit var blackTimer: TextView // Timer display for the black player
    private lateinit var whiteTimer: TextView // Timer display for the white player
    lateinit var blackPlayerData: TextView // TextView to display black player data
    lateinit var whitePlayerData: TextView // TextView to display white player data
    private var player = Player.White // Current player, default is White

    companion object Pictures {
        // Mapping of game pieces and board elements to drawable resources
        val pics = mapOf(
            "b" to R.drawable.black_squere,
            "w" to R.drawable.white_squere,
            "T" to R.drawable.target,
            "d" to R.drawable.dark_squere,
            Piece(Player.Black, false).toString() to R.drawable.b,
            Piece(Player.White, false).toString() to R.drawable.w,
            Piece(Player.Black, true).toString() to R.drawable.bq,
            Piece(Player.White, true).toString() to R.drawable.wq)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity_layout)

        // Initialize view components
        blackTimer = findViewById(R.id.tvBlackTimer)
        whiteTimer = findViewById(R.id.tvWhiteTimer)
        blackPlayerData = findViewById(R.id.tvBlackPlayerData)
        whitePlayerData = findViewById(R.id.tvWhitePlayerData)

        // Setup ViewModel and retrieve game instance
        val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
        val game = viewModel.game

        // Create and setup the game view using a factory pattern
        gameView = GameViewFactory.create(this, game, GameData.isOnline, GameData.gameType, GameData.myPlayer)

        // Update player data asynchronously if the game is online
        if (GameData.isOnline)
        {
            Thread {
                Thread.sleep(1300)
                handler.post {
                    blackPlayerData.text = "${viewModel.blackPlayer.userName}(${viewModel.blackPlayer.eloRanking})"
                    whitePlayerData.text = "${viewModel.whitePlayer.userName}(${viewModel.whitePlayer.eloRanking})"
                }
            }.start()
        }

        // Observe game state changes and update UI accordingly
        viewModel.turn.observe(this, gameView)
        viewModel.timeLeft.observe(this) {
            var otherTimer = blackTimer
            var currentTimer = whiteTimer
            if (game.turn == Player.Black)
            {
                otherTimer = whiteTimer
                currentTimer = blackTimer
            }
            currentTimer.text = String.format("%02d:%02d", it / 60, it % 60)
            otherTimer.text = "00:00"
            currentTimer.setBackgroundColor(getColor(R.color.black))
            otherTimer.setBackgroundColor(getColor(R.color.timerBackgroundColor))
        }

        viewModel.winner.observe(this) { winner ->
            winner?.let { endGame(it) }
        }

        // Add game view to layout
        llLayout = findViewById(R.id.ll)
        llLayout.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    /**
     * Displays a dialog to indicate game end and declare the winner.
     *
     * @param player Player who won the game.
     */
    private fun endGame(player: Player)
    {
        val dialog = AlertDialog.Builder(this)
            .setTitle("$player Player Won")
            .setPositiveButton("Leave") { _, _ ->
                onBackPressed()
            }
            .create()

        dialog.show()
    }
}
