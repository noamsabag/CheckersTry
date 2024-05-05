package com.example.checkerstry.classes

import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlin.math.max

object FirebaseUsersHelper {
    const val STARTING_ELO_RANKING = 800
    const val REGULAR_ELO_CHANGE = 8
    fun initUser(userId: String)
    {
        val dbRef = Firebase.database.getReference("$USERS_PATH/$userId")
        dbRef.child("eloRanking").setValue(STARTING_ELO_RANKING)
        dbRef.child("games").setValue(0)
        dbRef.child("gamesPlayed").setValue(0)
        dbRef.child("gamesWon").setValue(0)
    }

    fun loadUser(userId: String, user: com.example.checkerstry.classes.User? = null)
    {
        val dbRef = Firebase.database.getReference("$USERS_PATH/$userId")
        if (user == null)
        {
            dbRef.child("eloRanking").get().addOnCompleteListener { UserData.userId = it.result.getValue(String::class.java)!! }
            dbRef.child("gamesPlayed").get().addOnCompleteListener { UserData.gamesPlayed = it.result.getValue(Int::class.java)!! }
            dbRef.child("gamesWon").get().addOnCompleteListener { UserData.gamesWon = it.result.getValue(Int::class.java)!! }
        }
        else
        {
            dbRef.child("eloRanking").get().addOnCompleteListener { user.userId = it.result.getValue(String::class.java)!! }
            dbRef.child("gamesPlayed").get().addOnCompleteListener { user.gamesPlayed = it.result.getValue(Int::class.java)!! }
            dbRef.child("gamesWon").get().addOnCompleteListener { user.gamesWon = it.result.getValue(Int::class.java)!! }
        }
    }

    fun updateWin(enemyId: String, gameId: String)
    {
        val dbRef = Firebase.database.getReference("$USERS_PATH/${UserData.userId}")
        val enemyRef = Firebase.database.getReference("$USERS_PATH/$enemyId")
        UserData.gamesPlayed++
        UserData.gamesWon++
        dbRef.child("gamesPlayed").setValue(UserData.gamesPlayed)
        dbRef.child("gamesWon").setValue(UserData.gamesWon)
        enemyRef.child("gamesPlayed").get().addOnCompleteListener {
            try {
                enemyRef.child("gamesPlayed").setValue(it.result.getValue(Int::class.java)!! + 1)

            }
            catch (e: Exception)
            {
                val m = e.message.toString()
            }
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

    fun loadGameHistory(): ArrayList<FinishedGameData>
    {
        val arr = arrayListOf<FinishedGameData>()
        Firebase.database.getReference("$USERS_PATH/${UserData.userId}").child("games").orderByKey().get().addOnCompleteListener {
            it.result.children.forEach {game ->
                val winner = User()
                val looser = User()
                loadUser(game.child("winner").getValue(String::class.java)!!, winner)
                loadUser(game.child("looser").getValue(String::class.java)!!, looser)
                arr.add(FinishedGameData(winner, looser, it.result.child("gameType").getValue(GameType::class.java)!!, it.result.key!!))
            }

        }

        return arr
    }

}