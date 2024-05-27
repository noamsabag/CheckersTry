package com.example.checkerstry

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.checkerstry.classes.DarkGameView
import com.example.checkerstry.classes.GameActivityViewModel
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.GameView
import com.example.checkerstry.classes.Piece
import com.example.checkerstry.classes.Player
import com.example.checkerstry.classes.Game

class GameActivity : ComponentActivity()
{
    val handler = Handler(Looper.getMainLooper())
    lateinit var gameView: GameView
    lateinit var llLayout: LinearLayout
    lateinit var blackTimer: TextView
    lateinit var whiteTimer: TextView
    lateinit var blackPlayerData: TextView
    lateinit var whitePlayerData: TextView
    var player = Player.White

    companion object Pictures
    {
        val pics = mapOf<String, Int>(
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

        blackTimer = findViewById(R.id.tvBlackTimer)
        whiteTimer = findViewById(R.id.tvWhiteTimer)
        blackPlayerData = findViewById(R.id.tvBlackPlayerData)
        whitePlayerData = findViewById(R.id.tvWhitePlayerData)
        val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
        val game = viewModel.game
        gameView = GameViewFactory.create(this, game as Game, GameData.isOnline, GameData.gameType, GameData.myPlayer)

        if (GameData.isOnline)
        {
            object: Thread() {
                override fun run() {
                    sleep(1_300)
                    handler.post {
                        blackPlayerData.text = "${viewModel.blackPlayer.userName}(${viewModel.blackPlayer.eloRanking})"
                        whitePlayerData.text = "${viewModel.whitePlayer.userName}(${viewModel.whitePlayer.eloRanking})"
                    }
                }
            }.start()
        }

        viewModel.turn.observe(this, gameView)
        viewModel.timeLeft.observe(this) {
            var otherBtn = blackTimer
            var btn = whiteTimer
            if (game.turn == Player.Black)
            {
                otherBtn = whiteTimer
                btn = blackTimer
            }
            btn.text = String.format("%02d:%02d", it!! / 60, it % 60)
            otherBtn.text = "00:00"
            btn.setBackgroundColor(getColor(R.color.black))
            otherBtn.setBackgroundColor(getColor(R.color.timerBackgroundColor))
        }



        viewModel.winner.observe(this) {
            if (it != null)
            {
                endGame(it)
            }
        }
        llLayout = findViewById(R.id.ll)
        llLayout.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }


    fun endGame(player: Player)
    {
        val dialog = AlertDialog.Builder(this)
            .setTitle("${player.toString()} Player Won")
            .setPositiveButton("Leave") { _, _ ->
                super.onBackPressed()
            }
            .create()

        dialog.show()
    }

}

object GameViewFactory
{
    fun create(context: Context, game: Game, isOnline: Boolean, gameType: GameType, myPlayer: Player): GameView
    {
        if (isOnline)
        {
            return if (gameType == GameType.CheckersInTheDark)
            {
                DarkGameView(context, listOf(myPlayer), GameActivity.pics, game)
            } else
            {
                GameView(context, listOf(myPlayer), GameActivity.pics, game)
            }
        }
        else
        {
            return if (gameType == GameType.CheckersInTheDark)
            {
                DarkGameView(context, listOf(Player.White, Player.Black), GameActivity.pics, game)
            } else
            {
                GameView(context, listOf(Player.White, Player.Black), GameActivity.pics, game)
            }
        }
    }
}
