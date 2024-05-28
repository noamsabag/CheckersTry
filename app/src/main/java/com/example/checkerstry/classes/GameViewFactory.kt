package com.example.checkerstry.classes

import android.content.Context
import com.example.checkerstry.GameActivity

/**
 * Factory object to create GameView instances based on game settings such as online status and game type.
 */
object GameViewFactory {

    /**
     * Creates a GameView suitable for the specified game configuration.
     *
     * @param context The Context in which the view is being created, typically an Activity.
     * @param game The Game object representing the current game logic and state.
     * @param isOnline Indicates whether the game is being played online.
     * @param gameType The type of game being played, e.g., Regular or CheckersInTheDark.
     * @param myPlayer The Player who is using this view, important for online games to determine the perspective.
     * @return Returns a GameView or DarkGameView based on the game type and online status.
     */
    fun create(context: Context, game: Game, isOnline: Boolean, gameType: GameType, myPlayer: Player): GameView {
        // Decide between online and offline game view configuration.
        return if (isOnline)
        {
            // For online games, create views based on the game type and the player's perspective.
            when (gameType)
            {
                GameType.CheckersInTheDark -> DarkGameView(context, listOf(myPlayer), GameActivity.pics, game)
                else -> GameView(context, listOf(myPlayer), GameActivity.pics, game)
            }
        }
        else
        {
            // For offline games, always show both players since it's a local game.
            when (gameType)
            {
                GameType.CheckersInTheDark -> DarkGameView(context, listOf(Player.White, Player.Black), GameActivity.pics, game)
                else -> GameView(context, listOf(Player.White, Player.Black), GameActivity.pics, game)
            }
        }
    }
}
