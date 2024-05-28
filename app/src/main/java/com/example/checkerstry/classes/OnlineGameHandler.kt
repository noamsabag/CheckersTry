package com.example.checkerstry.classes

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Handles online game operations, including listening to and broadcasting moves using Firebase.
 *
 * @param game The current game instance containing game logic and state.
 * @param onlineGameData Data specific to the online gameplay, such as recorded moves.
 * @param player The player's own identifier, to differentiate between actions taken by the player and the opponent.
 * @param gameId The unique identifier for the game session, used to reference the correct database node.
 */
class OnlineGameHandler(var game: Game, var onlineGameData: OnlineGameData, val player: Player, val gameId: String) : Observer<Player> {
    // List to hold the moves recorded during the game.
    val moves: MutableList<Move> = mutableListOf()



    // Reference to the Firebase database path where game moves are stored.
    private val dbRef: DatabaseReference = Firebase.database.getReference("games/$gameId")

    init {
        // Listener to observe changes in the database, specifically the addition of new moves.
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                try {
                    // Check if the incoming move is not from the current turn's player to avoid replaying the same move.
                    if (game.turn != player) return
                    Log.w(TAG, "onChildAdded: ${dataSnapshot.key}")
                    val move = dataSnapshot.getValue(Move::class.java)!!
                    // Execute the move if it is not the last move already made.
                    if (move != game.lastMove) {
                        game.doMove(move)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    throw e
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved: ${dataSnapshot.key}")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved: ${dataSnapshot.key}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("moves").addChildEventListener(childEventListener)
    }

    /**
     * Observes changes in the player's turn, ensuring the game updates based on turn changes and the last move made.
     *
     * @param value The current player whose turn it is.
     */
    override fun onChanged(value: Player) {
        val turn = game.turn.previous()
        if (game.lastMove == null || turn  == player) return

        onlineGameData.movesDone.add(game.lastMove!!)
        updateGame()
    }

    /**
     * Updates the game by pushing the latest move to Firebase, ensuring all players see the move.
     */
    private fun updateGame() {
        try {
            val key = dbRef.child("moves").push().key!!
            dbRef.child("moves").child(key).setValue(onlineGameData.movesDone.last())
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            throw e
        }
    }
}
