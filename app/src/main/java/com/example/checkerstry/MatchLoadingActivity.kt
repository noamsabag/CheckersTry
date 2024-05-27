package com.example.checkerstry

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameState
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.GameTypeDictionary
import com.example.checkerstry.classes.QUICK_MATCH_PATH
import com.example.checkerstry.classes.RoomAdapter
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.ActivityMatchLoadingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlin.random.Random

class MatchLoadingActivity : AppCompatActivity() {
    private lateinit var thread: Thread
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityMatchLoadingBinding
    private val dbRef = Firebase.database.getReference(QUICK_MATCH_PATH)
    private var key = ""
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val roomId = snapshot.getValue(String::class.java)
            if (roomId!= null && roomId != "")
            {
                RoomAdapter.addPlayerToRoom(roomId, UserData.userId)
                dbRef.child(key).removeEventListener(this as ValueEventListener)
                dbRef.child(key).removeValue()
                RoomAdapter.startGame(roomId) {
                    val intent = Intent(this@MatchLoadingActivity, GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameType = GameData.gameType
        dbRef.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("gameType").getValue(GameType::class.java) == gameType)
                    {

                        val gameId = Random.nextInt(0, 9999).toString()

                        val valueEventListner = object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.getValue(GameState::class.java) == GameState.Ready)
                                {
                                    RoomAdapter.startGame(gameId) {
                                        val intent = Intent(this@MatchLoadingActivity, GameActivity::class.java)
                                        startActivity(intent)
                                    }                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(ContentValues.TAG, error.message.toString())
                                throw error.toException()
                            }

                        }

                        RoomAdapter.createRoom(gameId, gameType)
                        RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
                        Firebase.database.getReference("games/${gameId}/state").addValueEventListener(valueEventListner)
                        dbRef.child(it.key!!).child("roomId").setValue(gameId)

                        return
                    }
                }
                joinQueue()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        var numberOfDots = 0
        thread = object: Thread() {
            override fun run() {
                val dots = listOf(".", "..", "...")
                while (true)
                {
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
                    }
                    catch (e: Exception)
                    {
                        val t = e.message
                    }
                }
            }
        }
        thread.start()

        binding.btnCancel.setOnClickListener {
            try {
                dbRef.child(key).child("roomId").removeEventListener(valueEventListener)
                dbRef.child(key).removeValue()
            }
            catch (_: Exception) {}
            this.onBackPressed()
        }
    }


    fun joinQueue()
    {
        val gameType = GameData.gameType
        key = dbRef.push().key!!
        dbRef.child(key).child("gameType").setValue(gameType)
        dbRef.child(key).child("roomId").setValue("")
        dbRef.child(key).child("roomId").addValueEventListener(valueEventListener)
    }

    override fun onDestroy()
    {
        thread.interrupt()
        super.onDestroy()
    }
}