package com.example.checkerstry.classes

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OnlineGameHandler(var game: Game, var onlineGameData: OnlineGameData, val player: Player, val gameId: String) : Observer<Player> {
    val moves: MutableList<Move> = mutableListOf()
    private val players: MutableList<Player>
    private val dbRef: DatabaseReference = Firebase.database.getReference("games/${gameId}")

    init {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                try
                {
                    if (game.turn != player) return
                    Log.w(TAG, "onChildAdded:" + dataSnapshot.key!!)
                    val move = dataSnapshot.getValue<Move>()!!
                    if (move != game.lastMove)
                    {
                        game.doMove(dataSnapshot.getValue<Move>()!!)
                    }
                }
                catch (e : Exception)
                {
                    Log.e(TAG, e.message.toString())
                    throw e
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        dbRef.child("moves").addChildEventListener(childEventListener)
        players = mutableListOf(player)

    }

    override fun onChanged(value: Player) {
        val  turn = game.turn.previous()
        if (game.lastMove == null || turn in players)
            return

        onlineGameData.movesDone.add(game.lastMove!!)
        updateGame()

    }

    private fun updateGame() {
        try
        {
            val key = dbRef.child("moves").push().key!!
            dbRef.child("moves").child(key).setValue(onlineGameData.movesDone.last())

        } catch (e: Exception)
        {
            Log.e(TAG, e.message.toString())
            throw e
        }
    }

}