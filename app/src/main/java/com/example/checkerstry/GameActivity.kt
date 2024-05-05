package com.example.checkerstry

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import com.example.checkerstry.classes.GameActivityViewModel
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameState
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.GameView
import com.example.checkerstry.classes.IGame
import com.example.checkerstry.classes.MoveProvioderType
import com.example.checkerstry.classes.OnlineGameData
import com.example.checkerstry.classes.OnlineGameHandler
import com.example.checkerstry.classes.Piece
import com.example.checkerstry.classes.Player
import com.example.checkerstry.classes.RegularGame
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class GameActivity : ComponentActivity()
{
    lateinit var gameView: GameView
    lateinit var llLayout: LinearLayout
    lateinit var blackTimer: TextView
    lateinit var whiteTimer: TextView
    var player = Player.White

    companion object Pictures
    {
        val pics = mapOf<String, Int>(
            "b" to R.drawable.black_squere,
            "w" to R.drawable.white_squere,
            "T" to R.drawable.target,
            Piece(Player.Black, 0).toString() to R.drawable.b,
            Piece(Player.White, 0).toString() to R.drawable.w,
            Piece(Player.Black, 0).also { it.Queen() }.toString() to R.drawable.bq,
            Piece(Player.White, 0).also { it.Queen() }.toString() to R.drawable.wq)
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity_layout)

        blackTimer = findViewById(R.id.tvBlackTimer)
        whiteTimer = findViewById(R.id.tvWhiteTimer)
        val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
        val game = viewModel.game
        if (GameData.getMoveProviders().size == 0)
        {
            gameView = GameView(this, listOf(Player.Black, Player.White), 8, Pictures.pics, game as RegularGame)
        }
        else
        {
            gameView = GameView(this, listOf(GameData.getMoveProviders().keys.first().next()), 8, Pictures.pics, game as RegularGame)

        }
        viewModel.turn.observe(this, gameView)
        viewModel.timeLeft.observe(this) {
            var otherBtn = blackTimer
            var btn = whiteTimer
            if (game.turn.value == Player.Black)
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

    }

}
