package com.example.checkerstry.classes

import android.content.ContentValues
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.Exception

class GameActivityViewModel() : ViewModel() {

    val game: IGame = GameFactory.create(GameData.getGameType()) { _, _, newValue ->
            _turn.value = newValue
    }
    val moves = arrayListOf<Move>()
    val blackPlayer = User()
    val whitePlayer = User()
    private val _turn = MutableLiveData<Player>(Player.White)
    val turn: LiveData<Player>
        get() = _turn

    private val moveProviders: MutableList<MoveProvider> = mutableListOf()
    private val _timeLeft = MutableLiveData<Int>(0)
    lateinit var timer: CountDownTimer
    val timeLeft: LiveData<Int>
        get() = _timeLeft
    private val _winner = MutableLiveData<Player?>(null)
    val winner: LiveData<Player?>
        get() = _winner


    companion object MoveProviderFactory {

        fun create(player: Player, moveProvioderType: MoveProvioderType, game: IGame, gameId: String) : MoveProvider {
            if (moveProvioderType == MoveProvioderType.ONLINE)
            {
                return OnlineGameHandler(game, OnlineGameData(), player, gameId)
            }
            else if (moveProvioderType == MoveProvioderType.CPU)
            {
                return CpuPlayer(game, player)
            }
            return OnlineGameHandler(game, OnlineGameData(), player, gameId)
        }
    }


    init
    {

        try {
            _timeLeft.value = 0
        }
        catch (e: Exception)
        {
            val r = e.message
        }
        GameData.getMoveProviders().forEach {
            if (it.value == MoveProvioderType.ONLINE)
            {
                Firebase.database.getReference("$GAMES_PATH/${GameData.getGameId()}/players").addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        FirebaseUsersHelper.loadUser(snapshot.children.first().getValue(String::class.java)!!, whitePlayer)
                        FirebaseUsersHelper.loadUser(snapshot.children.last().getValue(String::class.java)!!, blackPlayer)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
            moveProviders.add(MoveProviderFactory.create(it.key, it.value, game, GameData.getGameId()))
        }

        moveProviders.forEach {
            turn.observeForever(it)
        }

        turn.observeForever{
            if (game.lastMove != null)
            {
                moves.add(game.lastMove!!.copy())
                timer.cancel()
            }
            val res = game.isFinal()
            if (res != null)
            {
                endGame(res)
            }
            timer = object: CountDownTimer(62_000, 1_000){
                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished < 2_500)
                    {
                        onFinish()
                        return
                    }
                    _timeLeft.value = ((millisUntilFinished - 2_000) / 1000).toInt()
                }

                override fun onFinish() {
                    if (GameData.getMoveProviders().keys.size == 0 || turn.value != GameData.getMoveProviders().keys.first())
                    {
                        game.doMove(game.getAllMoves().random())
                    }
                    else
                    {
                        endGame(turn.value!!.next())
                    }
                }
            }.start()

        }
    }
    fun endGame(player: Player)
    {
        if (GameData.getMoveProviders().keys.size > 0 && player != GameData.getMoveProviders().keys.first())
        {
            Firebase.database.getReference("$GAMES_PATH/${GameData.getGameId()}/players").get().addOnCompleteListener {
                it.result.children.forEach{player ->
                    val id = player.getValue(String::class.java)!!
                    if (id != UserData.userId)
                    {
                        FirebaseUsersHelper.updateWin(id, GameData.getGameId())
                    }
                }
            }
        }
        _winner.value = player
    }

}

