package com.example.checkerstry.classes

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.example.checkerstry.GameActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.values
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

object RoomAdapter
{
    fun createRoom(gameId: String, gameType: GameType)
    {
        try {
            val dbRef = Firebase.database.getReference("games/${gameId}")
            dbRef.child("gameType").setValue(gameType)
            dbRef.child("state").setValue(GameState.WaitingForPlayers)
            dbRef.child("players").setValue(0)
            dbRef.child("playersNum").setValue(0)
        }
        catch (e : Exception)
        {
            Log.e(ContentValues.TAG, e.message.toString())
        }
    }

    fun addPlayerToRoom(gameId: String, playerId: String)
    {
        try
        {
            val dbRef = Firebase.database.getReference("games/${gameId}")
            val key = dbRef.child("players").push().key!!
            dbRef.child("players").child(key).setValue(playerId)
            dbRef.child("playersNum").get().addOnCompleteListener{
                increasePlayerCount(it.result, gameId)
            }

        }
        catch (e : Exception)
        {
            Log.e(ContentValues.TAG, e.message.toString())
            throw e
        }

    }

    fun increasePlayerCount(dataSnapshot: DataSnapshot, gameId: String)
    {
        val dbRef = Firebase.database.getReference("games/${gameId}")
        val num = dataSnapshot.getValue(Int::class.java)!! + 1
        if (num == 2)
        {
            dbRef.child("state").setValue(GameState.Ready)
        }
        dbRef.child("playersNum").setValue(num)
    }
    fun startGame(gameId: String, openGame: () -> Unit)
    {
        val dbRef = Firebase.database.getReference("games/${gameId}/players")
        GameData.setGameId(gameId)
        Firebase.database.getReference("games/${gameId}").child("gameType").get().addOnCompleteListener {
            try {
                GameData.setGameType(it.result.getValue(GameType::class.java)!!)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.message.toString())
                throw e
            }
            dbRef.get().addOnCompleteListener {
                if (it.result.children.first()
                        .getValue(String::class.java) == UserData.userId
                ) {
                    GameData.addMoveProovidor(Player.Black, MoveProvioderType.ONLINE)
                } else {
                    GameData.addMoveProovidor(Player.White, MoveProvioderType.ONLINE)
                }
                openGame()
            }
        }
    }
}