package com.example.checkerstry.classes

/**
 * Represents a user within the application, storing various properties such as user identification,
 * game-related statistics, and an Elo ranking.
 *
 * @param userId The unique identifier for the user, defaults to an empty string if not provided.
 * @param userName The display name of the user, defaults to an empty string if not provided.
 * @param gamesPlayed The total number of games the user has played, defaults to 0 if not provided.
 * @param gamesWon The total number of games the user has won, defaults to 0 if not provided.
 * @param eloRanking The user's Elo ranking, a method for calculating the relative skill levels of players, defaults to 0 if not provided.
 */
class User(
    var userId: String = "",
    var userName: String = "",
    var gamesPlayed: Int = 0,
    var gamesWon: Int = 0,
    var eloRanking: Int = 0
)
