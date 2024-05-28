package com.example.checkerstry.classes

import java.util.Stack
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Represents the main game logic for a checkers game, managing turns, moves, and game state.
 *
 * @param size The size of the game board, indicating how many squares per side.
 * @param onTurnChanged A callback function that is called whenever the turn changes. It provides the old and new player.
 */
class Game(val size: Int, onTurnChanged: (KProperty<*>, Player, Player) -> Unit = { _, _, _ -> }) {
    // Observable property to manage player turns. Notifies a callback function whenever the turn changes.
    var turn: Player by Delegates.observable(Player.White) { property, oldValue, newValue ->
        onTurnChanged(property, oldValue, newValue)
    }

    private val previousMoves = Stack<Move>()

    // Stores the last move made in the game.
    val lastMove: Move?
        get() = if (previousMoves.isEmpty()) null else previousMoves.lastElement()


    // List of all moves made during the game.
    private var moves = mutableListOf<Move>()

    // Indicates whether the moves list is initialized.
    private var isMovesInitialized = false

    // Represents the game board.
    var board: Board = Board(size)

    /**
     * Executes a specified move on the game board, updating the game state accordingly.
     *
     * @param move The move to execute.
     */
    fun doMove(move: Move) {
        val saver = board[move.pos]
        board[move.pos] = null
        board[move.steps.last()] = saver

        move.eaten.keys.forEach { key ->
            board[key] = null
        }

        if (move.queen) {
            board[move.steps.last()] = Piece(board[move.steps.last()]!!.player, true)
        }

        isMovesInitialized = false
        moves.clear()
        previousMoves.push(move.copy())
        turn = turn.next()
    }

    /**
     * Calculates potential moves from a specified board position, considering both simple moves and capturing sequences.
     * This function handles the move logic for both regular pieces and queens, accounting for multiple jumping capabilities.
     *
     * @param pos The board position from which to calculate moves.
     * @param directions Optional list of specific directions to check; if null, directions are determined by the piece's position and type.
     * @param isChain Indicates if the move calculation is part of a chain capture, affecting the processing of subsequent jumps.
     * @return A list of valid moves from the specified position.
     */
    private fun basicGetMoves(pos: Pos, directions: List<Pos>? = null, isChain: Boolean = false): List<Move> {
        val piece = board[pos] ?: return listOf()  // If no piece at position, return an empty list.
        val moves: MutableList<Move> = mutableListOf()
        val dirs = directions ?: getDirections(pos)  // Determine movement directions based on the piece type and position.
        var reach = 1  // Normal piece movement reach.
        var eatingReach = 2  // Normal piece eating (capturing) reach.
        if (piece.isQueen) {
            reach = size  // Queens can move across the entire board.
            eatingReach = size  // Queens can capture across the entire board.
        }

        // Process each direction for possible moves.
        for (direction in dirs)
        {
            var i = 1
            var opponentsPassed = 0
            var alliesPassed = 0
            // Explore the direction for possible simple moves or capturing opportunities.
            while ((!isChain) && i <= reach && board.isPosLegal(pos + direction * i) && opponentsPassed == 0 && alliesPassed == 0)
            {
                if (board[pos + direction * i] == null)
                {
                    moves.add(moveof(pos, pos + direction * i))  // Add simple move.
                } else if (board[pos + direction * i]?.player == piece.player)
                {
                    alliesPassed++  // Stop if an ally is encountered.
                } else
                {
                    opponentsPassed++  // Count opponent pieces passed.
                }
                i++
            }
            // Explore further for capturing moves.
            while (i <= eatingReach && board.isPosLegal(pos + direction * i) && opponentsPassed <= 1 && alliesPassed == 0)
            {
                if (board[pos + direction * i] == null && opponentsPassed == 1)
                {
                    board[pos + direction * i] = board[pos]
                    board[pos] = null
                    var saver: Piece? = null
                    var changedAt = 1
                    for (j in 1 until i)
                    {
                        if (board[pos + direction * j] != null)
                        {
                            saver = board[pos + direction * j]
                            board[pos + direction * j] = null
                            changedAt = j
                            break
                        }
                    }
                    val newMoves = this.basicGetMoves(pos + direction * i, dirs, true) // Recursive call for chained captures.
                    newMoves.forEach{it.steps.add(0, it.pos); it.pos = pos + direction * i; it.eaten.put(pos + direction * changedAt, saver!!)}
                    moves.addAll(newMoves)
                    board[pos + direction * changedAt] = saver
                    board[pos] = board[pos + direction * i]
                    board[pos + direction * i] = null
                }
                else if (board[pos + direction * i] != null)
                {
                    if (board[pos + direction * i]!!.player == piece.player)
                    {
                        alliesPassed++
                    }
                    else
                    {
                        opponentsPassed++
                    }
                }
                i++
            }
        }

        if (isChain)
        {
            val m = Move(pos)
            moves.add(m)
        }
        // Adjust moves for queen promotion based on end position.
        moves.forEach {
            it.pos = Pos(pos)
            if (!isChain && it.steps.last().y == size - 1 && !board[it.pos]!!.isQueen) {
                it.queen = true
            }
        }
        return moves
    }



    /**
     * Retrieves all valid moves from a specified position on the board.
     *
     * @param pos The position to retrieve moves for.
     * @return A list of valid moves from the specified position.
     */
    fun getMoves(pos: Pos): List<Move> {
        initMoves()
        return moves.filter { it.pos == pos }
    }

    /**
     * Retrieves all valid moves for all pieces currently active for the current player's turn.
     *
     * @return A list of all valid moves for the current turn.
     */
    fun getAllMoves(): List<Move> {
        initMoves()
        return moves.toList()
    }

    /**
     * Determines if the game has reached a final state where one player has won.
     *
     * @return The winning player if the game is over, otherwise null if the game is ongoing.
     */
    fun isFinal(): Player? {
        var white = false
        var black = false
        for (i in 0 until size) {
            for (j in 0 until size) {
                when {
                    board[i, j]?.player == Player.White -> white = true
                    board[i, j]?.player == Player.Black -> black = true
                }
            }
        }
        return when {
            white && black -> null
            white -> Player.White
            else -> Player.Black
        }
    }

    /**
     * Initializes or updates the list of valid moves based on the current game state.
     */
    private fun initMoves() {
        if (!isMovesInitialized) {
            moves.clear()
            for (pos in getPoses()) {
                if (board[pos]?.player == turn) {
                    moves.addAll(basicGetMoves(pos))
                }
            }
            val maxMoveSize = moves.maxByOrNull { it.eaten.keys.size }?.eaten?.keys?.size ?: 0
            moves = moves.filter { it.eaten.keys.size >= maxMoveSize }.toMutableList()
            isMovesInitialized = true
        }
    }

    /**
     * Reverses the last move made, restoring the previous state.
     *
     * @param move The move to undo.
     */
    fun unDoMove(move: Move) {
        board[move.pos] = board[move.steps.last()]
        board[move.steps.last()] = null
        move.eaten.keys.forEach { pos ->
            board[pos] = move.eaten[pos]
        }

        if (move.queen) {
            board[move.steps.last()] = Piece(board[move.steps.last()]!!.player, false)
        }
        isMovesInitialized = false
        moves.clear()
        previousMoves.pop()
        turn = turn.previous()
    }

    /**
     * Generates a list of all board positions.
     *
     * @return A list of positions representing each square on the board.
     */
    private fun getPoses(): List<Pos> = List(size * size) { i -> Pos(i / size, i % size) }

    /**
     * Determines the legal movement directions for a piece based on its type and player.
     * Regular pieces move diagonally forward relative to their starting side, while queens can move in any diagonal direction.
     *
     * @param pos The position of the piece for which to determine directions.
     * @return A list of possible directions (as Pos objects) that the piece at the given position can move towards.
     */
    private fun getDirections(pos: Pos): List<Pos> {
        val piece = board[pos] ?: return emptyList()  // Return an empty list if there is no piece at the given position

        return when {
            piece.isQueen -> {
                // Queens move diagonally in all four directions
                listOf(Pos(1, 1), Pos(1, -1), Pos(-1, 1), Pos(-1, -1))
            }
            piece.player == Player.Black -> {
                // Black regular pieces move diagonally forward (down the board if standard orientation is assumed)
                listOf(Pos(1, -1), Pos(-1, -1))
            }
            else -> {
                // White regular pieces move diagonally forward (up the board if standard orientation is assumed)
                listOf(Pos(1, 1), Pos(-1, 1))
            }
        }
    }

}
