package com.example.checkerstry

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
import kotlin.random.Random

class MatchLoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameType = GameType.RegularGame
        val dbRef = Firebase.database.getReference(QUICK_MATCH_PATH)
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
    }


    fun joinQueue()
    {
        UserData.userId = "2"
        val gameType = GameType.RegularGame

        val dbRef = Firebase.database.getReference(QUICK_MATCH_PATH)
        val key = dbRef.push().key!!
        dbRef.child(key).child("gameType").setValue(gameType)
        dbRef.child(key).child("roomId").setValue("")
        dbRef.child(key).child("roomId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomId = snapshot.getValue(String::class.java)!!
                if (roomId != "")
                {
                    RoomAdapter.addPlayerToRoom(roomId, UserData.userId)
                    RoomAdapter.startGame(roomId) {
                        val intent = Intent(this@MatchLoadingActivity, GameActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}