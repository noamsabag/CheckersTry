package com.example.checkerstry.classes

import android.content.Context

/**
 * A specialized version of the GameView for playing "Dark Game", where players can only see certain parts of the board
 * around their pieces, simulating limited visibility or "fog of war".
 *
 * @param c The context where the game view is being created, usually the current activity.
 * @param players The list of players participating in the game.
 * @param pictures A map of string identifiers to drawable resource IDs for the different types of pieces.
 * @param game The game logic and state manager, providing access to current game data.
 */
class DarkGameView(c: Context, players: List<Player>, pictures: Map<String, Int>, game: Game) : GameView(c, players, pictures, game) {

    /**
     * Overrides the base class's updateBoard method to implement the visibility logic specific to the Dark Game.
     * Only certain squares around the player's pieces and their potential moves are made visible.
     */
    override fun updateBoard() {
        val dark = Array(boardSize) { Array(boardSize) { false } }

        // Loop through all buttons to determine which should be illuminated based on the position of the player's pieces.
        for (i in buttons.indices) {
            for (j in buttons[0].indices) {
                if (buttons[i][j].piece?.player == game.turn) {
                    dark[i][j] = true
                    lightSurrounding(dark, j, i)  // Light up surrounding squares.
                }
            }
        }

        // Illuminate potential move positions.
        for (move in game.getAllMoves()) {
            for (pos in move.steps) {
                dark[pos.y][pos.x] = true
            }
        }

        // Apply the visibility settings to the buttons.
        for (i in buttons.indices) {
            for (j in buttons[0].indices) {
                buttons[i][j].isNotDark = dark[i][j]
            }
        }

        super.updateBoard()  // Call the base method to handle other aspects of board updating.
    }

    /**
     * Illuminates the squares surrounding a specified square.
     * This method assumes a checkers board where each piece lights up its immediate diagonal and straight neighbors.
     *
     * @param mat A two-dimensional array representing the visibility of each square on the board.
     * @param x The x-coordinate (column index) of the square whose neighbors are to be illuminated.
     * @param y The y-coordinate (row index) of the square whose neighbors are to be illuminated.
     */
    fun lightSurrounding(mat: Array<Array<Boolean>>, x: Int, y: Int) {
        // Light up directly adjacent squares and diagonally adjacent squares.
        if (y - 1 >= 0) {
            mat[y - 1][x] = true
            if (x - 1 >= 0) {
                mat[y - 1][x - 1] = true
            }
            if (x + 1 < game.size) {
                mat[y - 1][x + 1] = true
            }
        }
        if (y + 1 < game.size) {
            mat[y + 1][x] = true
            if (x - 1 >= 0) {
                mat[y + 1][x - 1] = true
            }
            if (x + 1 < game.size) {
                mat[y + 1][x + 1] = true
            }
        }
        if (x + 1 < game.size) {
            mat[y][x + 1] = true
        }
        if (x - 1 >= 0) {
            mat[y][x - 1] = true
        }
    }
}
