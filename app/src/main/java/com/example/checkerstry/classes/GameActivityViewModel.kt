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

/**
 * ViewModel associated with GameActivity, handling the dynamic aspects of a game session
 * such as game state, turn management, player data, and time left for moves.
 */
class GameActivityViewModel : ViewModel() {
    // Game instance, initialized from GameFactory based on the current game type.
    val game: Game = GameFactory.create(GameData.gameType) { _, _, newValue ->
        _turn.value = newValue
    }

    // Optional handler for online game interactions; only initialized if the game is online.
    private var onlinePlayer: OnlineGameHandler? = null

    // List to track all moves made during the game.
    private val moves = arrayListOf<Move>()

    // User data for players, initially empty and populated from Firebase.
    val blackPlayer = User()
    val whitePlayer = User()

    // LiveData to track and notify changes in the current turn.
    private val _turn = MutableLiveData(Player.White)
    val turn: LiveData<Player>
        get() = _turn

    // LiveData for managing the countdown timer's time left for the current turn.
    private val _timeLeft = MutableLiveData<Int>(0)
    lateinit var timer: CountDownTimer
    val timeLeft: LiveData<Int>
        get() = _timeLeft

    // LiveData to hold and notify the winner of the game.
    private val _winner = MutableLiveData<Player?>(null)
    val winner: LiveData<Player?>
        get() = _winner

    init {
        _timeLeft.value = 0

        // Setup online game components if the game is set to be online.
        if (GameData.isOnline) {
            Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}/players").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    FirebaseUsersHelper.loadUser(snapshot.children.first().getValue(String::class.java)!!, whitePlayer)
                    FirebaseUsersHelper.loadUser(snapshot.children.last().getValue(String::class.java)!!, blackPlayer)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            onlinePlayer = OnlineGameHandler(game, OnlineGameData(), GameData.myPlayer.next(), GameData.gameId)
            turn.observeForever(onlinePlayer!!)
        }

        // Observe turn changes to handle game logic and end-game conditions.
        turn.observeForever {
            if (game.lastMove != null) {
                moves.add(game.lastMove!!.copy())
                timer.cancel()
            }
            val res = game.isFinal()
            if (res != null) {
                endGame(res)
            } else {
                // Setup and start the game timer for the new turn.
                timer = object : CountDownTimer(62_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        _timeLeft.value = ((millisUntilFinished - 2_000) / 1000).toInt()
                    }

                    override fun onFinish() {
                        endGame(turn.value!!.next())
                    }
                }.start()
            }
        }
    }

    /**
     * Ends the game, handles cleanup and updates Firebase based on the game result.
     *
     * @param player The player who is determined to be the winner or next player if the timer runs out.
     */
    fun endGame(player: Player) {
        if (GameData.isOnline) {
            turn.removeObserver(onlinePlayer!!)
        }
        if (GameData.isOnline && player == GameData.myPlayer) {
            Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}/players").get().addOnCompleteListener {
                it.result.children.forEach { player ->
                    val id = player.getValue(String::class.java)!!
                    if (id != UserData.userId) {
                        FirebaseUsersHelper.updateWin(id, GameData.gameId)
                    }
                }
                // Thread to delay and then clear game data from Firebase.
                object : Thread() {
                    override fun run() {
                        sleep(2_000)
                        Firebase.database.getReference("$GAMES_PATH/${GameData.gameId}").removeValue()
                    }
                }.start()
            }
        }
        _winner.value = player
    }
}
