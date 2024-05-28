package com.example.checkerstry.classes

import kotlin.reflect.KProperty

/**
 * Factory object for creating different types of games based on specified game types.
 * This allows for easy instantiation of game variants with custom configurations such as board size.
 */
object GameFactory {

    /**
     * Creates a game instance according to the specified game type.
     * Each game type can have a different board size and initial setup, which are defined here.
     *
     * @param gameType The type of game to create, defined by the GameType enum.
     * @param onTurnChanged A callback function that gets called whenever the turn changes in the game.
     *                      This allows external components to react to turn changes.
     * @return A new instance of Game configured according to the specified game type.
     */
    fun create(gameType: GameType, onTurnChanged: (KProperty<*>, Player, Player) -> Unit = { _, _, _ ->}): Game {
        return when (gameType) {
            GameType.RegularGame -> Game(8, onTurnChanged)  // Standard checkers game with an 8x8 board.
            GameType.SmallGame -> Game(4, onTurnChanged)   // A smaller variant of the game with a 4x4 board.
            GameType.BigGame -> Game(12, onTurnChanged)    // A larger variant with a 12x12 board for extended play.
            GameType.CheckersInTheDark -> Game(8, onTurnChanged)  // Regular game with additional rules or setup that may mimic playing in the dark.
        }
    }
}
