package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.checkerstry.classes.*
import com.example.checkerstry.databinding.ActivityMatchLoadingBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

/**
 * An activity to handle the matching process in a game, allowing players to join or create a game room.
 */
class MatchLoadingActivity : AppCompatActivity() {
    private lateinit var thread: Thread
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityMatchLoadingBinding
    private val dbRef = Firebase.database.getReference(QUICK_MATCH_PATH)
    private var key = ""

    // ValueEventListener to manage room joining and game start.
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val roomId = snapshot.getValue(String::class.java)
            if (roomId != null && roomId != "") {
                RoomAdapter.addPlayerToRoom(roomId, UserData.userId)
                dbRef.child(key).removeEventListener(this)
                dbRef.child(key).removeValue()
                RoomAdapter.startGame(roomId) {
                    val intent = Intent(this@MatchLoadingActivity, GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MatchLoadingActivity", "Error in matchmaking ValueEventListener: ${error.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameType = GameData.gameType

        // Initiating a search for an available game room of the same type.
        dbRef.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("gameType").getValue(GameType::class.java) == gameType) {
                        var gameId = Random.nextInt(0, 9999).toString()
                        while (RoomAdapter.isRoomTaken(gameId)) {
                            gameId = Random.nextInt(0, 9999).toString()
                        }

                        // Setting up listener for the game state.
                        val valueEventListener = object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.getValue(GameState::class.java) == GameState.Ready) {
                                    RoomAdapter.startGame(gameId) {
                                        val intent = Intent(this@MatchLoadingActivity, GameActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("MatchLoadingActivity", "Error in matchmaking ValueEventListener: ${error.message}")

                            }
                        }

                        RoomAdapter.createRoom(gameId, gameType)
                        RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
                        Firebase.database.getReference("games/$gameId/state").addValueEventListener(valueEventListener)
                        dbRef.child(it.key!!).child("roomId").setValue(gameId)

                        return
                    }
                }
                joinQueue()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MatchLoadingActivity", "Error in matchmaking ValueEventListener: ${error.message}")
            }
        })

        // Animation for searching players.
        var numberOfDots = 0
        thread = object : Thread() {
            override fun run() {
                val dots = listOf(".", "..", "...")
                while (true) {
                    numberOfDots++
                    numberOfDots %= 3
                    try {
                        handler.post {
                            binding.tvSearchingForPlayers.text = buildString {
                                append("searching for players ")
                                append(dots[numberOfDots])
                            }
                        }
                        sleep(500)
                    } catch (e: Exception) {
                        val t = e.message
                    }
                }
            }
        }
        thread.start()

        // Cancel button to stop searching and exit.
        binding.btnCancel.setOnClickListener {
            try {
                dbRef.child(key).child("roomId").removeEventListener(valueEventListener)
                dbRef.child(key).removeValue()
            } catch (_: Exception) {
            }
            this.onBackPressed()
        }
    }

    /**
     * Joins the queue for game matching by registering the player's desire to join a game.
     */
    fun joinQueue() {
        val gameType = GameData.gameType
        key = dbRef.push().key!!
        dbRef.child(key).child("gameType").setValue(gameType)
        dbRef.child(key).child("roomId").setValue("")
        dbRef.child(key).child("roomId").addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        thread.interrupt()
        super.onDestroy()
    }
}
