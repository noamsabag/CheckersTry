package com.example.checkerstry.classes

import android.content.Context
import android.view.View
import android.widget.GridLayout
import androidx.lifecycle.Observer

/**
 * Represents the graphical user interface for the checkers game, managing the display and interactions within the game board.
 *
 * @param c The context where the game view is being created, usually the current activity.
 * @param players The list of players participating in the game, determining who can interact with the board.
 * @param pictures A map of string identifiers to drawable resource IDs for the different types of pieces.
 * @param game The game logic and state manager, providing access to current game data.
 */
open class GameView(c: Context, val players: List<Player>, pictures: Map<String, Int>, val game: Game) : GridLayout(c), View.OnClickListener, Observer<Player> {
    val boardSize = game.size  // The size of the game board, derived from the game state.
    var buttons: List<List<CheckersButton>>  // Two-dimensional list of buttons representing the checkers pieces on the board.
    private var selectedPiece: CheckersButton? = null  // Tracks the currently selected piece, if any.

    init {
        super.setRowCount(boardSize)  // Set the number of rows in the grid to match the board size.
        super.setColumnCount(boardSize)  // Set the number of columns in the grid to match the board size.

        val temp = mutableListOf<List<CheckersButton>>()  // Temporary storage for button rows.
        for (i in 0 until boardSize) {
            val row = mutableListOf<CheckersButton>()
            for (j in 0 until boardSize) {
                val pos = Pos(j, boardSize - 1 - i)  // Position of the button in grid terms.
                val newBtn = CheckersButton(c, pictures, pos, game.board[pos], boardSize)
                newBtn.setOnClickListener(this)
                row.add(newBtn)
                this.addView(newBtn)  // Add the button to the GridLayout.
            }
            temp.add(0, row.toList())  // Add the row at the beginning to align with visual top-to-bottom layout.
        }
        buttons = temp.toList()  // Finalize the structure of the button grid.
        updateBoard()  // Initial update to set the pieces on the board.
    }

    /**
     * Handles updates when the game state changes, typically triggered by player turn changes.
     *
     * @param value The player whose turn it currently is.
     */
    override fun onChanged(value: Player) {
        updateBoard()  // Redraw the board whenever the game state changes.
    }

    /**
     * Updates the visual state of the board, syncing it with the current state of the game logic.
     */
    protected open fun updateBoard() {
        for (i in buttons.indices) {
            for (j in buttons[0].indices) {
                buttons[i][j].piece = game.board[i, j]  // Update the piece on each button.
                buttons[i][j].isTarget = false  // Reset target status.
                buttons[i][j].updateImage()  // Refresh the button's image to reflect the current state.
            }
        }
    }

    /**
     * Handles user interactions with the game board.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        if (v is CheckersButton) {
            val btn: CheckersButton = v
            if (btn.piece?.player in players && btn.piece?.player == game.turn) {
                updateBoard()  // Deselect any previously selected pieces and update possible moves.
                val moves = game.getMoves(btn.pos)  // Get all valid moves for the selected piece.
                moves.forEach { move ->
                    val lastMove = move.steps.last()
                    buttons[lastMove.y][lastMove.x].isTarget = true  // Mark the end positions of moves as targets.
                    buttons[lastMove.y][lastMove.x].updateImage()
                }
                selectedPiece = btn  // Set the selected piece for further actions.
            } else if (btn.isTarget && selectedPiece != null) {
                val move = game.getMoves(selectedPiece!!.pos).find { it.steps.last() == btn.pos }  // Find the specific move performed.
                move?.let {
                    game.doMove(it)  // Execute the move in the game logic.
                    selectedPiece = null  // Clear the selection.
                    updateBoard()  // Update the board to reflect the new state.
                }
            }
        }
    }
}
