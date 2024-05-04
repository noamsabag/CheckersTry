package com.example.checkerstry.classes

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.checkerstry.GameActivity
import com.example.checkerstry.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class GameActivityViewModel() : ViewModel() {

    val game: IGame = RegularGame(8)
    val moves = arrayListOf<Move>()
    val turn: LiveData<Player>
        get() = game.turn
    private val moveProviders: MutableList<MoveProvider> = mutableListOf()

    companion object MoveProviderFactory {

        fun create(player: Player, moveProvioderType: MoveProvioderType, game: IGame, gameId: String) : MoveProvider {
            if (moveProvioderType == MoveProvioderType.ONLINE)
            {
                return OnlineGameHandler(game, OnlineGameData(), player, gameId)
            }
            return OnlineGameHandler(game, OnlineGameData(), player, gameId)
        }
    }


    init
    {
        GameData.getMoveProviders().forEach {
            moveProviders.add(MoveProviderFactory.create(it.key, it.value, game, GameData.getGameId()))
        }

        moveProviders.forEach {
            turn.observeForever(it)
        }

        turn.observeForever{
            if (game.lastMove != null) moves.add(game.lastMove!!.copy())
            val res = game.isFinal()
            if (res != null)
            {
                endGame(res)
            }
        }
    }
    fun initOnlineData()
    {

        val onlineGameData = OnlineGameData()
        val dbRef = Firebase.database.getReference("games/${GameData.getGameId()}")
        try
        {
            dbRef.child("moves").setValue(0)
        }
        catch (e: Exception)
        {
            Log.e(ContentValues.TAG, e.message.toString())
            throw e
        }
    }

    fun endGame(player: Player)
    {
        runBlocking { delay(1000) }

    }

}