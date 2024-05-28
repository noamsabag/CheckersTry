package com.example.checkerstry.classes

/**
 * Enum class representing players in a game, specifically for games with two opposing sides like checkers or chess.
 */
enum class Player {
    /** Enum constant representing the White player. */
    White,

    /** Enum constant representing the Black player. */
    Black;

    /**
     * Returns the opponent player.
     * @return the next player in the sequence. If the current player is White, it returns Black, and vice versa.
     */
    fun next() = if (this == White) Black else White

    /**
     * Returns the opponent player, reusing the next function.
     * @return the opponent player, effectively toggling between White and Black.
     */
    fun previous() = next()

    /**
     * Provides a string representation of the player.
     * @return "White" if the player is White, "Black" if the player is Black.
     */
    override fun toString(): String = if (this == White) "White" else "Black"
}

/**
 * Class representing a piece in a checkers game, which can be a regular piece or a queen.
 * @param player the player to whom the piece belongs, defaulting to White.
 * @param isQueen a Boolean indicating whether the piece is a queen, defaulting to false.
 */
class Piece(val player: Player = Player.White, val isQueen: Boolean = false) {

    /**
     * Provides a string representation of the piece, denoting both the player and the type of the piece.
     * @return a string that indicates the player's color and the piece type. "WQ" for White Queen, "BQ" for Black Queen,
     * "B " for Black, and "W " for White.
     */
    override fun toString(): String {
        return if (isQueen) {
            if (player == Player.White) "WQ" else "BQ"
        } else {
            if (player == Player.Black) "B " else "W "
        }
    }
}
