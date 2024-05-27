package com.example.checkerstry

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameState
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.GameTypeDictionary
import com.example.checkerstry.classes.Move
import com.example.checkerstry.classes.Player
import com.example.checkerstry.classes.RoomAdapter
import com.example.checkerstry.classes.UserData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class RoomsActivity : AppCompatActivity() {

    lateinit var btnCreateRoom: Button
    lateinit var btnJoinRoom: Button
    lateinit var spnGameTypeSelector: Spinner
    lateinit var etRoomNumber: EditText
    lateinit var dialog: Dialog
    lateinit var gameId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rooms_activity)

        btnCreateRoom = findViewById(R.id.btnCreateRoom)
        btnJoinRoom = findViewById(R.id.btnJoinRoom)
        spnGameTypeSelector = findViewById(R.id.spnGameTypeSelector)
        etRoomNumber = findViewById(R.id.etRoomNumber)


        btnJoinRoom.setOnClickListener{
            joinRoom()
        }




        btnCreateRoom.setOnClickListener{
           createRoom()
        }
    }



    fun createRoom()
    {
        val valueEventListner = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(GameState::class.java) == GameState.Ready)
                {
                    startGame()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message.toString())
                throw error.toException()
            }

        }

        gameId = Random.nextInt(0, 9999).toString()
        val gameType = GameTypeDictionary.dictionary[spnGameTypeSelector.selectedItem.toString()]!!

        RoomAdapter.createRoom(gameId, gameType)
        RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
        Firebase.database.getReference("games/${gameId}/state").addValueEventListener(valueEventListner)
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.waiting_room_layout)
        val tvRoomId: TextView = dialog.findViewById(R.id.tvRoomId)
        tvRoomId.text = "Room Id:${gameId}"
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn : Button = dialog.findViewById(R.id.btnFragmentLeave)

        btn.setOnClickListener{
            val dbRef = Firebase.database.getReference("games")
            dbRef.child(gameId).removeValue()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun joinRoom()
    {
        val dbRef = Firebase.database.getReference("games")
        gameId = etRoomNumber.text.toString()
        val e = 1
        dbRef.child(gameId).get().addOnCompleteListener {
            try {
                if (it.isSuccessful)
                {
                    RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
                    startGame()
                }
                else
                {
                    Toast.makeText(this, "Room Doesn't Exists", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception)
            {
                val m = e.message
            }
        }
    }
    fun startGame()
    {
        try {
            RoomAdapter.startGame(gameId) {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }
        catch(e : Exception)
        {
            Log.e(ContentValues.TAG, e.message.toString())
            throw e
        }

    }
}