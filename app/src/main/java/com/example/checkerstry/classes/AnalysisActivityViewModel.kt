package com.example.checkerstry.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Stack

/**
 * ViewModel for managing the analysis of a game session.
 * Responsible for retrieving and manipulating game moves for analysis purposes.
 */
class AnalysisActivityViewModel: ViewModel() {

    // Initializing the game instance with a specific game type and a turn change listener.
    val game = GameFactory.create(GameData.gameType) { _, _, newValue ->
        _turn.value = newValue
    }

    // Storing the original moves retrieved from the database for playback.
    val originalMoves: MutableList<Move> = mutableListOf()

    // Index of the current move being analyzed.
    var currentMove: Int = -1

    // Private mutable live data to track the current turn's player.
    private val _turn = MutableLiveData<Player>(Player.White)

    // Public immutable live data to expose the current turn's player.
    val turn: LiveData<Player>
        get() = _turn

    init {
        // Referencing the game data in Firebase and loading all moves into the originalMoves list.
        val dbRef = Firebase.database.getReference("$GAME_STORAGE_PATH/${GameData.gameId}")
        dbRef.child("moves").get().addOnCompleteListener {
            it.result.children.forEach { move ->
                originalMoves.add(move.getValue(Move::class.java)!!)
            }
        }
    }

    /**
     * Advances the move index and applies the next move to the game.
     * Allowing the user to step forward through the moves for analysis.
     */
    fun moveForward() {
        currentMove++
        game.doMove(originalMoves[currentMove].copy())
    }

    /**
     * Decreases the move index and reverses the last move in the game.
     * Allowing the user to step backward through the moves for analysis.
     */
    fun moveBack() {
        game.unDoMove(originalMoves[currentMove].copy())
        currentMove--
    }
}
