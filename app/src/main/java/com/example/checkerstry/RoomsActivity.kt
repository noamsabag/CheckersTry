package com.example.checkerstry

import android.app.Dialog
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
import com.example.checkerstry.classes.GameState
import com.example.checkerstry.classes.GameTypeDictionary
import com.example.checkerstry.classes.RoomAdapter
import com.example.checkerstry.classes.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

/**
 * Activity for handling the creation and joining of game rooms for multiplayer matches.
 */
class RoomsActivity : AppCompatActivity() {
    private lateinit var btnCreateRoom: Button
    private lateinit var btnJoinRoom: Button
    private lateinit var spnGameTypeSelector: Spinner
    private lateinit var etRoomNumber: EditText
    private lateinit var dialog: Dialog
    lateinit var gameId: String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rooms_activity)

        // Initialize UI components
        btnCreateRoom = findViewById(R.id.btnCreateRoom)
        btnJoinRoom = findViewById(R.id.btnJoinRoom)
        spnGameTypeSelector = findViewById(R.id.spnGameTypeSelector)
        etRoomNumber = findViewById(R.id.etRoomNumber)

        // Set onClickListeners for the buttons
        btnCreateRoom.setOnClickListener {
            createRoom()
        }
        btnJoinRoom.setOnClickListener {
            joinRoom()
        }
    }

    /**
     * Creates a new game room and adds the current user as a participant.
     */
    private fun createRoom() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(GameState::class.java) == GameState.Ready) {
                    startGame()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Firebase error: ${error.message}")
            }
        }

        // Generate a unique game ID
        gameId = Random.nextInt(0, 9999).toString()
        while (RoomAdapter.isRoomTaken(gameId)) {
            gameId = Random.nextInt(0, 9999).toString()
        }
        val gameType = GameTypeDictionary.dictionary[spnGameTypeSelector.selectedItem.toString()]!!

        // Create room in Firebase and add player
        RoomAdapter.createRoom(gameId, gameType)
        RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
        Firebase.database.getReference("games/$gameId/state").addValueEventListener(valueEventListener)

        // Setup and show the waiting room dialog
        dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.waiting_room_layout)
            findViewById<TextView>(R.id.tvRoomId).text = "Room Id: $gameId"
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            findViewById<Button>(R.id.btnFragmentLeave).setOnClickListener {
                Firebase.database.getReference("games").child(gameId).removeValue()
                dismiss()
            }
        }
        dialog.show()
    }

    /**
     * Attempts to join an existing game room using a room ID specified by the user.
     */
    private fun joinRoom()
    {
        val dbRef = Firebase.database.getReference("games")
        gameId = etRoomNumber.text.toString()
        dbRef.child(gameId).get().addOnCompleteListener {
            try {
                if (it.isSuccessful) {
                    RoomAdapter.addPlayerToRoom(gameId, UserData.userId)
                    startGame()
                } else {
                    Toast.makeText(this, "Room Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error joining room: ${e.message}")
            }
        }
    }

    /**
     * Initiates the game session, transitioning to the game activity.
     */
    fun startGame()
    {
        try
        {
            RoomAdapter.startGame(gameId) {
                startActivity(Intent(this, GameActivity::class.java))
            }
        } catch (e: Exception)
        {
            Log.e(TAG, "Error starting game: ${e.message}")
            throw e
        }
    }
}
