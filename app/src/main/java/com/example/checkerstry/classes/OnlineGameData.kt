package com.example.checkerstry.classes

/**
 * Enum representing the possible states of a game, particularly useful in managing game flow in an online context.
 */
enum class GameState {
    /** Indicates that the game is currently waiting for players to join. */
    WaitingForPlayers,

    /** Indicates that the game is ready to start. */
    Ready
}

/**
 * Data class representing the completed game's data.
 * This includes the winner and loser of the game, the type of game, and a unique game identifier.
 *
 * @param winner The user who won the game. Defaults to an empty User instance if not specified.
 * @param looser The user who lost the game. Defaults to an empty User instance if not specified.
 * @param gameType The type of game that was played, defaults to RegularGame if not specified.
 * @param gameId The unique identifier for the game.
 */
data class FinishedGameData(
    val winner: User = User(),
    val looser: User = User(),
    var gameType: GameType = GameType.RegularGame,
    var gameId: String = ""
)

/**
 * Data class representing the ongoing data of an online game.
 * Stores a list of moves that have been performed during the game.
 *
 * @param movesDone An ArrayList of Move objects representing the sequence of moves done in the game.
 */
data class OnlineGameData(
    var movesDone: ArrayList<Move> = arrayListOf()
)
