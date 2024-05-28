package com.example.checkerstry.classes

import android.content.Context
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.appcompat.widget.AppCompatImageButton

/**
 * A custom button for a checkers board game that displays different images based on its state.
 *
 * @param c The context in which the button is being used, typically the current activity.
 * @param pictures A map linking string keys to drawable resource IDs for the images to display.
 * @param pos The position of the button on the grid.
 * @param piece The piece that is currently on this button's position on the board, if any.
 * @param boardSize The size of the board, used to calculate the button's size.
 */
class CheckersButton(c: Context, private val pictures: Map<String, Int>, pos: Pos, var piece: Piece?, boardSize: Int) :
    AppCompatImageButton(c) {

    // Calculated size for the button based on the board size.
    private val size = (127 * (8.0 / boardSize.toDouble())).toInt()

    // Default tile color.
    private var tileColor = Player.White

    // Indicates whether this button is a target for a possible move.
    var isTarget = false

    // Indicates whether the tile is visible or "not dark".
    var isNotDark = true

    // Position of the button on the board.
    val pos: Pos = pos
        get() = Pos(field)

    init {
        // Set layout parameters and image scaling type.
        super.setLayoutParams(GridLayout.LayoutParams(ViewGroup.LayoutParams(size, size)))
        super.setPadding(0, 0, 0, 0)
        super.setScaleType(ScaleType.CENTER_CROP)

        // Initialize the image based on the position's parity and set the initial tile color.
        if ((pos.x + pos.y) % 2 == 0) {
            tileColor = Player.Black
            super.setImageResource(pictures["b"]!!)
        } else {
            tileColor = Player.White
            super.setImageResource(pictures["w"]!!)
        }
    }

    /**
     * Updates the image displayed on the button based on the current state of the tile.
     */
    fun updateImage() {
        when {
            !isNotDark -> super.setImageResource(pictures["d"]!!)
            tileColor == Player.White -> super.setImageResource(pictures["w"]!!)
            isTarget -> super.setImageResource(pictures["T"]!!)
            piece == null -> super.setImageResource(pictures["b"]!!)
            else -> super.setImageResource(pictures[piece.toString()]!!)
        }
    }
}
