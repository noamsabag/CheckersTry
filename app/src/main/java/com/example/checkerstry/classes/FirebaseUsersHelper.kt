package com.example.checkerstry.classes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.max
/**
 * Utility object to handle Firebase operations related to user data and game histories.
 */
object FirebaseUsersHelper {
    private const val STARTING_ELO_RANKING = 800
    private const val REGULAR_ELO_CHANGE = 8

    // List to store completed game data for the current user.
    val gamesPlayed: ArrayList<FinishedGameData> = arrayListOf()
    /**
     * Initializes user data in Firebase when a new user is created or logged in for the first time.
     *
     * @param userId The unique identifier of the user.
     * @param userName The name of the user.
     */
    fun initUser(userId: String, userName: String)
    {
        UserData.userId = userId
        UserData.userName = userName
        UserData.eloRanking = STARTING_ELO_RANKING
        UserData.gamesPlayed = 0
        UserData.gamesWon = 0
        val dbRef = Firebase.database.getReference("$USERS_PATH/$userId")
        dbRef.child("userName").setValue(userName)
        dbRef.child("eloRanking").setValue(STARTING_ELO_RANKING)
        dbRef.child("games").setValue(0)
        dbRef.child("gamesPlayed").setValue(0)
        dbRef.child("gamesWon").setValue(0)
    }
    /**
     * Loads user data from Firebase and updates the local UserData or specified User object.
     *
     * @param userId The unique identifier of the user to load.
     * @param user An optional User object to be populated with the data. If null, updates the global UserData.
     */
    fun loadUser(userId: String, user: User? = null)
    {
        UserData.userId = userId
        val dbRef = Firebase.database.getReference("$USERS_PATH/$userId")
        if (user == null)
        {
            UserData.userId = userId
            dbRef.child("userName").get().addOnCompleteListener { UserData.userName = it.result.getValue(String::class.java)!! }
            dbRef.child("eloRanking").get().addOnCompleteListener { UserData.eloRanking = it.result.getValue(Int::class.java)!! }
            dbRef.child("gamesPlayed").get().addOnCompleteListener { UserData.gamesPlayed = it.result.getValue(Int::class.java)!! }
            dbRef.child("gamesWon").get().addOnCompleteListener { UserData.gamesWon = it.result.getValue(Int::class.java)!! }
        }
        else
        {
            user.userId = userId

            dbRef.child("userName").get().addOnCompleteListener {
                user.userName = it.result.getValue(String::class.java)!!
            }

            dbRef.child("eloRanking").get().addOnCompleteListener {
                user.eloRanking = it.result.getValue(Int::class.java)!!
            }

            dbRef.child("gamesPlayed").get().addOnCompleteListener {
                user.gamesPlayed = it.result.getValue(Int::class.java)!!
            }

            dbRef.child("gamesWon").get().addOnCompleteListener {
                user.gamesWon = it.result.getValue(Int::class.java)!!
            }
        }
    }
    /**
     * Updates win statistics for the current user and the opponent, adjusting Elo rankings accordingly.
     *
     * @param enemyId The unique identifier of the opponent.
     * @param gameId The unique identifier of the game.
     */
    fun updateWin(enemyId: String, gameId: String)
    {
        val dbRef = Firebase.database.getReference("$USERS_PATH/${UserData.userId}")
        val enemyRef = Firebase.database.getReference("$USERS_PATH/$enemyId")
        UserData.gamesPlayed++
        UserData.gamesWon++
        dbRef.child("gamesPlayed").setValue(UserData.gamesPlayed)
        dbRef.child("gamesWon").setValue(UserData.gamesWon)
        enemyRef.child("gamesPlayed").get().addOnCompleteListener {
            enemyRef.child("gamesPlayed").setValue(it.result.getValue(Int::class.java)!! + 1)
        }
        enemyRef.child("eloRanking").get().addOnCompleteListener {
            val enemyRanking = it.result.getValue(Int::class.java)!!
            val rankingDelta = max(1, REGULAR_ELO_CHANGE - (UserData.eloRanking - enemyRanking) / 100)
            UserData.eloRanking = UserData.eloRanking + rankingDelta
            dbRef.child("eloRanking").setValue(UserData.eloRanking)
            enemyRef.child("eloRanking").setValue(enemyRanking - rankingDelta)
        }
        Firebase.database.getReference("$GAMES_PATH/$gameId").get().addOnCompleteListener {
            val snapshot = it.result
            val newRef = Firebase.database.getReference(GAME_STORAGE_PATH).push()
            newRef.child("moves").setValue(0)
            newRef.child("winner").setValue(UserData.userId)
            newRef.child("looser").setValue(enemyId)
            newRef.child("gameType").setValue(snapshot.child("gameType").getValue(GameType::class.java))
            enemyRef.child("games").child(newRef.key!!).setValue(0)
            dbRef.child("games").child(newRef.key!!).setValue(1)
            snapshot.child("moves").children.forEach {move ->
                val key = newRef.child("moves").push().key!!
                newRef.child("moves/%$key").setValue(move.getValue(Move::class.java))
            }
        }
    }
    /**
     * Loads the game history of the current user from Firebase.
     */
    fun loadGameHistory()
    {

        Firebase.database.getReference("$USERS_PATH/${UserData.userId}/games").orderByKey().addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    gamesPlayed.add(loadGameData(it.key!!))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    /**
     * Loads game data from Firebase based on the provided game ID.
     *
     * @param gameId The unique identifier of the game.
     * @return The game data as a FinishedGameData object.
     */
    fun loadGameData(gameId: String): FinishedGameData
    {
        val dbRef = Firebase.database.getReference("$GAME_STORAGE_PATH/$gameId")
        val winner = User()
        val looser = User()
        val game = FinishedGameData(winner, looser, GameType.RegularGame, gameId)


        dbRef.child("winner").get().addOnCompleteListener {
            loadUser(it.result.getValue(String::class.java)!!, winner)
        }

        dbRef.child("looser").get().addOnCompleteListener {
            loadUser(it.result.getValue(String::class.java)!!, looser)
        }

        dbRef.child("gameType").get().addOnCompleteListener {
            game.gameType = it.result.getValue(GameType::class.java)!!
        }

        return game
    }



}