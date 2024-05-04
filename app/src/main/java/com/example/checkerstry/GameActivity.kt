package com.example.checkerstry

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.checkerstry.ui.theme.CheckersTryTheme
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
    lateinit var llLayout: LinearLayoutCompat
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
        llLayout = findViewById<LinearLayoutCompat>(R.id.ll)
        llLayout.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    fun initOnlineData()
    {
        val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
        //val onlineGameData = OnlineGameData(mutableMapOf(), viewModel.game.value!!, "1", GameState.Ongoing)
        val onlineGameData = OnlineGameData()
        val dbRef = Firebase.database.getReference("games/1")
        try
        {
            dbRef.child("moves").setValue(0)
        }
        catch (e:Exception)
        {
            Log.e(TAG, e.message.toString())
            throw e
        }
        initGame(onlineGameData)
    }

    fun initGame(onlineGameData: OnlineGameData)
    {
        val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
        val game = viewModel.game
        gameView = GameView(this, listOf(player), 8, Pictures.pics, game as RegularGame)
        val onlineGameHandler = OnlineGameHandler(game, onlineGameData, player.next(), "1")
        viewModel.turn.observe(this, onlineGameHandler)
        viewModel.turn.observe(this, gameView)
        llLayout = findViewById<LinearLayoutCompat>(R.id.ll)
        llLayout.addView(gameView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    }

    suspend fun sod()
    {
        try
        {
            val t = Firebase.firestore.collection("new").document("1").get().await()
            if (t != null)
            {
                var th = t.toObject(OnlineGameData::class.java)
            }
        }
        catch (e: Exception)
        {
            val st = e.toString()
            val viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)
            //val onlineGameData = OnlineGameData(mutableMapOf(), viewModel.game.value!!, "1", GameState.Ongoing)
            val onlineGameData = OnlineGameData()
            player = Player.Black
            Firebase.firestore.collection("new").document("1").set(onlineGameData).await()
        }
    }


}
