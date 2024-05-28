package com.example.checkerstry.classes

/**
 * Represents the game board checkers game.
 * @param size the dimension of the square game board.
 */
class Board(val size: Int) {
    /**
     * The 2D array representing the game board, initialized with null values indicating empty spaces.
     */
    private val board = Array(size) { Array<Piece?>(size) { null }
    }

    /**
     * Initializes the game board by placing pieces for both players at the start of the game.
     * The pieces are arranged such that they occupy every other square and only on specific rows depending on the board size.
     */
    init {
        for (i in 0 until size * size) {
            if ((i / size + i % size) % 2 == 0) {
                if (i < size * (size / 2 - 1)) {
                    board[i / size][i % size] = Piece(Player.White, false)
                } else if (i >= size * (size / 2 + 1)) {
                    board[i / size][i % size] = Piece(Player.Black, false)
                }
            }
        }
    }

    /**
     * Allows getting a piece at a specified position using its row and column indices.
     * @param y the row index.
     * @param x the column index.
     * @return the piece at the specified position, or null if the position is empty.
     */
    operator fun get(y: Int, x: Int): Piece? = board[y][x]

    /**
     * Allows getting a piece at a position specified by a Pos object.
     * @param pos the position object.
     * @return the piece at the specified position, or null if the position is empty.
     */
    operator fun get(pos: Pos): Piece? = board[pos.y][pos.x]

    /**
     * Allows setting a piece at a specified position using its row and column indices.
     * @param y the row index.
     * @param x the column index.
     * @param piece the piece to place at the specified position, can be null to indicate removing a piece.
     */
    operator fun set(y: Int, x: Int, piece: Piece?) {
        board[y][x] = piece
    }

    /**
     * Allows setting a piece at a position specified by a Pos object.
     * @param pos the position object.
     * @param piece the piece to place at the specified position, can be null to indicate removing a piece.
     */
    operator fun set(pos: Pos, piece: Piece?) {
        board[pos.y][pos.x] = piece
    }

    /**
     * Checks if a given position is within the bounds of the board.
     * @param pos the position to check.
     * @return true if the position is within the board's bounds, otherwise false.
     */
    fun isPosLegal(pos: Pos): Boolean = pos.x in 0 until size && pos.y in 0 until size

    /**
     * Creates a deep copy of the board, duplicating its current state.
     * @return a new Board instance with the same size and pieces positioned as in this board.
     */
    fun copy(): Board {
        val output = Board(size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                output[i, j] = null
                this[i, j]?.let {
                    output[i, j] = it
                }
            }
        }
        return output
    }
}
