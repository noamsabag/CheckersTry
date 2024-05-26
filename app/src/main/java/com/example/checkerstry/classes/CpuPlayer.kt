package com.example.checkerstry.classes

import android.os.Handler
import android.os.Looper
import kotlin.math.abs
import kotlin.math.pow

class CpuPlayer(var game: IGame, val player: Player) : MoveProvider
{
    val handler = Handler(Looper.getMainLooper())

    override fun onChanged(value: Player) {
        if (value == player)
        {
            game.doMove(getBestMove())
            //MovePlayer(game).start()
        }
    }

    private  fun getBestMove(): Move
    {
        val workingGame = this.game.copy()
        var bestRating: Int = 0
        var bestMove = Move()
        val moves = workingGame.getAllMoves()
        if (workingGame.turn == Player.White)
        {
            bestRating = Int.MIN_VALUE
            moves.forEach {move ->
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, 6)
                if (rating > bestRating)
                {
                    bestRating = rating
                    bestMove = move
                }
                workingGame.unDoMove(move)
            }
        }
        else
        {
            bestRating = Int.MAX_VALUE
            moves.forEach {move ->
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, 6)
                if (rating < bestRating)
                {
                    bestRating = rating
                    bestMove = move
                }
                workingGame.unDoMove(move)
            }
        }


        return bestMove
    }

    private fun rateBoard(workingGame: IGame, depth : Int = 8, alpha: Int = Int.MIN_VALUE, beta: Int = Int.MAX_VALUE) : Int
    {
        var _alpha = alpha
        var _beta = beta
        if (depth == 0)
        {
            val t = workingGame.getRating()
            return t
        }
        var bestRating = 0
        if (workingGame.turn == Player.White)
        {
            bestRating = Int.MIN_VALUE
            val moves =  workingGame.getAllMoves()
            for (move in moves)
            {
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, depth - 1)
                bestRating = maxOf(rating, bestRating)
                workingGame.unDoMove(move)
                _alpha = maxOf(_alpha, rating)
                if (_beta <= alpha)
                {
                    break
                }
            }
        }
        else
        {
            bestRating = Int.MAX_VALUE
            val moves =  workingGame.getAllMoves()
            for (move in moves)
            {
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, depth - 1)
                bestRating = minOf(rating, bestRating)
                workingGame.unDoMove(move)
                _beta = minOf(_beta, rating)
                if (_beta <= _alpha)
                {
                    break
                }
            }

        }



        return bestRating
    }
}

class Game
{
    val board: LiteBoard = LiteBoard()


    fun getMoves(): List<LiteBoard>
    {
        val moves = mutableListOf<LiteBoard>()
        var eatOnly = false
        for (i in 0 until 32)
        {
            val pos = Pos(2 * i % 8, 2 * i / 8)
            if (board[pos] == 1)
            {
                getRegularEatMoves(pos, board).also {
                    if (it.isNotEmpty())
                    {
                        moves.addAll(it)
                        eatOnly = true
                    }
                }
                if (eatOnly) continue
                if (board[pos + Pos(1, 1)] == 0)
                {
                    moves.add(LiteBoard(flipIndexes(board.pieces, getIndex(pos), getIndex(pos + Pos(1, 1)))))
                }
                if (board[pos + Pos(-1, 1)] == 0)
                {
                    moves.add(LiteBoard(flipIndexes(board.pieces, getIndex(pos), getIndex(pos + Pos(-1, 1)))))

                }
            }
        }
        return moves
    }

    fun getRegularEatMoves(pos: Pos, board: LiteBoard): List<LiteBoard>
    {
        val moves = mutableListOf<LiteBoard>()
       for (dir in listOf(Pos(1, 1), Pos(-1, 1)))
       {
           if (board[pos + dir] == -1 && board[pos + dir * 2] == 0)
           {
               moves.addAll(getRegularEatMoves(pos + dir * 2,
                   LiteBoard(
                       flipIndexes(board.pieces,
                           getIndex(pos),
                           getIndex(pos + dir) + 32,
                           getIndex(pos + dir * 2)), board.queens)))
           }
       }
        if (moves.isEmpty())
        {
            moves.add(board)
        }

        return moves
    }

    fun getQueenEatMoves(pos: Pos, board: LiteBoard): List<LiteBoard>
    {
        for (dir in listOf(Pos(1, 1), Pos(-1, 1), Pos(1, -1), Pos(-1, -1)))
        {
            val currPos = pos
            while (currPos.x in 0..<8 && currPos.y in 0..<8)
            {
                if (board[pos] != 0)
                {
                    break
                }

            }
        }
        return listOf()
    }

    fun move(startPos: Pos, endPos: Pos, board: LBoard) = makeChanges(board,
            listOf(Pair(endPos, board[startPos]),
            Pair(startPos, 0)))
}

fun getIndex(pos: Pos) = pos.x / 2 + pos.y * 4

fun flipIndexes(board: ULong, vararg indexes: Int): ULong
{
    var out = board
    for (i in indexes)
    {
        val x: ULong = 1u
        out = out xor (x shl i - 1)
    }

    return out
}

fun flipIndexes(board: UInt, vararg indexes: Int): UInt
{
    var out = board
    for (i in indexes)
    {
        val x: UInt = 1u
        out = out xor (x shl i - 1)
    }

    return out
}

fun makeChanges(board: LBoard, changes: List<Pair<Pos, Int>>): LBoard
{
    var whitePieces = board.whitePieces
    var blackPieces = board.blackPieces
    var queens = board.queens

    for (pair in changes)
    {
        if (pair.second > 0)
        {
            whitePieces = setBit(whitePieces, pair.first, 1)
            blackPieces = setBit(blackPieces, pair.first, 0)
            queens = setBit(queens, pair.first, pair.second - 1)
        }
        else
        {
            whitePieces = setBit(whitePieces, pair.first, 0)
            blackPieces = setBit(blackPieces, pair.first, 1)
            queens = setBit(queens, pair.first, abs(pair.second + 1))
        }
    }

    return LBoard(whitePieces, blackPieces, queens)
}