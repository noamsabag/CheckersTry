package com.example.checkerstry.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Stack

class AnalysisActivityViewModel: ViewModel() {
    val game: Game = Game(8) { property, oldValue, newValue ->
        _turn.value = newValue
    }
    val originalMoves: MutableList<Move> = mutableListOf()
    var currentMove: Int = -1
    val additionalMoves: Stack<Move> = Stack()
    val _turn = MutableLiveData<Player>(Player.White)
    val turn: LiveData<Player>
        get() = _turn
    init {
        val dbRef = Firebase.database.getReference("$GAME_STORAGE_PATH/${GameData.gameId}")
        dbRef.child("moves").get().addOnCompleteListener {
            it.result.children.forEach {move ->
                originalMoves.add(move.getValue(Move::class.java)!!)
            }
        }

    }

    fun moveForward()
    {
        currentMove++
        game.doMove(originalMoves[currentMove].copy())
    }

    fun moveBack()
    {
        game.unDoMove(originalMoves[currentMove].copy())
        currentMove--
    }

}