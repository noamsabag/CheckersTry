package com.example.checkerstry.classes

/**
 * Singleton object that holds and manages user-specific data throughout the application.
 * This data includes user identification, game statistics, and login credentials.
 */
object UserData {
    /** Unique identifier for the user. */
    var userId: String = "1"

    /** Display name of the user. */
    var userName: String = "User Name 1"

    /** Total number of games played by the user. */
    var gamesPlayed: Int = 0

    /** Total number of games won by the user. */
    var gamesWon: Int = 0

    /** Elo ranking of the user, a method for calculating the relative skill levels of players. */
    var eloRanking: Int = 0

    /** Email address associated with the user's account. */
    var email = ""

    /** Password for the user's account, used for authentication. */
    var password = ""

    /**
     * Resets all user data to default values.
     * This method is typically called during a logout process or when resetting user data.
     */
    fun reset() {
        userId = ""
        userName = ""
        gamesPlayed = 0
        gamesWon = 0
        eloRanking = 0
        email = ""
        password = ""
    }
}
