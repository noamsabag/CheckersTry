package com.example.checkerstry.classes

/**
 * Enum representing the different types of games available in the application.
 */
enum class GameType {
    /** Standard game format. */
    RegularGame,

    /** A variation of checkers played with limited visibility of the board. */
    CheckersInTheDark,

    /** A smaller, quicker version of the game. */
    SmallGame,

    /** An extended version of the game with a larger board. */
    BigGame
}

/**
 * Singleton object that maps string descriptions to their corresponding GameType enum values.
 * This facilitates easy retrieval and usage of game types based on string inputs.
 */
object GameTypeDictionary {
    /** A HashMap linking string descriptions of game types to their corresponding enum values. */
    val dictionary = hashMapOf(
        "Regular Game" to GameType.RegularGame,
        "Checkers In The Dark" to GameType.CheckersInTheDark,
        "Small Game" to GameType.SmallGame,
        "Big Game" to GameType.BigGame
    )
}

/**
 * Singleton object to manage game-specific data throughout the application.
 * This includes settings like game type, online status, player role, and game ID.
 */
object GameData {
    /** The type of game currently set. Defaults to RegularGame. */
    var gameType: GameType = GameType.RegularGame

    /** Flag indicating whether the game is played online. Defaults to false. */
    var isOnline = false

    /** Represents which player the user is playing as. Defaults to White. */
    var myPlayer = Player.White

    /** Unique identifier for the game session. Defaults to an empty string. */
    var gameId = ""
}
