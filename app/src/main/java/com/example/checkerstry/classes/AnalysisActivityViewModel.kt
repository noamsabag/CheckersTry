package com.example.checkerstry.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Stack

class AnalysisActivityViewModel: ViewModel() {
    val game: IGame = RegularGame(8)
    val originalMoves: MutableList<Move> = mutableListOf()
    var currentMove: Int = -1
    val additionalMoves: Stack<Move> = Stack()
    val turn: LiveData<Player>
        get() = game.turn
    init {
        val dbRef = Firebase.database.getReference("$GAME_STORAGE_PATH/${GameData.getGameId()}")
        dbRef.child("moves").get().addOnCompleteListener {
            it.result.children.forEach {move ->
                originalMoves.add(move.getValue(Move::class.java)!!)
            }
        }

    }



}