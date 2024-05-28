package com.example.checkerstry.classes

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

/**
 * Manages game room operations in Firebase, providing functionalities such as creating rooms,
 * adding players, and starting games.
 */
object RoomAdapter {

    /**
     * Checks if a game room with the specified gameId already exists in the database.
     *
     * @param gameId Unique identifier for the game room.
     * @return Boolean indicating whether the room is already taken.
     */
    fun isRoomTaken(gameId: String): Boolean = runBlocking {
        val data = Firebase.database.getReference(GAMES_PATH).child(gameId).get().await()
        return@runBlocking data.exists()
    }

    /**
     * Creates a new game room in Firebase with initial settings for game type and player count.
     *
     * @param gameId The unique identifier for the game room.
     * @param gameType The type of the game to be set up in the room.
     */
    fun createRoom(gameId: String, gameType: GameType) {
        try {
            val dbRef = Firebase.database.getReference("games/$gameId")
            dbRef.child("gameType").setValue(gameType)
            dbRef.child("state").setValue(GameState.WaitingForPlayers)
            dbRef.child("players").setValue(0)
            dbRef.child("playersNum").setValue(0)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, e.message.toString())
        }
    }

    /**
     * Adds a player to an existing room, updating the player count and room state accordingly.
     *
     * @param gameId The unique identifier for the game room.
     * @param playerId The unique identifier for the player.
     */
    fun addPlayerToRoom(gameId: String, playerId: String) {
        try {
            val dbRef = Firebase.database.getReference("games/$gameId")
            val key = dbRef.child("players").push().key!!
            dbRef.child("players").child(key).setValue(playerId)
            dbRef.child("playersNum").get().addOnCompleteListener {
                increasePlayerCount(it.result, gameId)
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, e.message.toString())
            throw e
        }
    }

    /**
     * Increases the count of players in the room and updates the game state to ready if the required number of players is reached.
     *
     * @param dataSnapshot The snapshot containing the current number of players.
     * @param gameId The unique identifier for the game room.
     */
    private fun increasePlayerCount(dataSnapshot: DataSnapshot, gameId: String) {
        val dbRef = Firebase.database.getReference("games/$gameId")
        val num = dataSnapshot.getValue(Int::class.java)!! + 1
        if (num == 2) { // Assuming a two-player game
            dbRef.child("state").setValue(GameState.Ready)
        }
        dbRef.child("playersNum").setValue(num)
    }

    /**
     * Starts the game by determining the player's color based on the order in Firebase and invoking the provided function to open the game.
     *
     * @param gameId The unique identifier for the game room.
     * @param openGame A function to be invoked to open/start the game activity.
     */
    fun startGame(gameId: String, openGame: () -> Unit) {
        val dbRef = Firebase.database.getReference("games/$gameId/players")
        GameData.gameId = gameId
        Firebase.database.getReference("games/$gameId").child("gameType").get().addOnCompleteListener { task ->
            try {
                GameData.gameType = task.result.getValue(GameType::class.java)!!
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.message.toString())
                throw e
            }
            dbRef.get().addOnCompleteListener { playersResult ->
                if (playersResult.result.children.first().getValue(String::class.java) == UserData.userId) {
                    GameData.isOnline = true
                    GameData.myPlayer = Player.White
                } else {
                    GameData.isOnline = true
                    GameData.myPlayer = Player.Black
                }
                openGame()
            }
        }
    }
}
