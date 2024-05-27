package com.example.checkerstry.classes

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameActivityViewModel : ViewModel() {

    val game: Game = GameFactory.create(GameData.gameType) { _, _, newValue ->
            _turn.value = newValue
    }
    private val moves = arrayListOf<Move>()
    val blackPlayer = User()
    val whitePlayer = User()
    private val _turn = MutableLiveData(Player.White)
    val turn: LiveData<Player>
        get() = _turn

    private val _timeLeft = MutableLiveData<Int>(0)
    lateinit var timer: CountDownTimer
    val timeLeft: LiveData<Int>
        get() = _timeLeft
    private val _winner = MutableLiveData<Player?>(null)
    val winner: LiveData<Player?>
        get() = _winner

    init
    {
        _timeLeft.value = 0

        if (GameData.isOnline)
        {
            Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}/players").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    FirebaseUsersHelper.loadUser(snapshot.children.first().getValue(String::class.java)!!, whitePlayer)
                    FirebaseUsersHelper.loadUser(snapshot.children.last().getValue(String::class.java)!!, blackPlayer)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            turn.observeForever(OnlineGameHandler(game, OnlineGameData(), GameData.myPlayer.next(), GameData.gameId))
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
                    if (millisUntilFinished < 2_500 && (!GameData.isOnline || game.turn == GameData.myPlayer))
                    {
                        onFinish()
                        timer.cancel()
                        return
                    }
                    _timeLeft.value = ((millisUntilFinished - 2_000) / 1000).toInt()
                }

                override fun onFinish() {
                    endGame(turn.value!!.next())
                }
            }.start()

        }
    }
    fun endGame(player: Player)
    {
        if (GameData.isOnline && player == GameData.myPlayer)
        {
            Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}/players").get().addOnCompleteListener {
                it.result.children.forEach{player ->
                    val id = player.getValue(String::class.java)!!
                    if (id != UserData.userId)
                    {
                        FirebaseUsersHelper.updateWin(id, GameData.gameId)
                    }
                }
            }

            object : Thread() {
                override fun run() {
                    sleep(2_000)
                    Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}").removeValue()
                }
            }
        }
        _winner.value = player
    }

}

