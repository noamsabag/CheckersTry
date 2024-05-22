package com.example.checkerstry.classes

import android.os.Handler
import android.os.Looper
import kotlin.properties.Delegates

class CpuPlayer(var game: IGame, val player: Player) : MoveProvider, Thread()
{
    val handler = Handler(Looper.getMainLooper())

    override fun onChanged(value: Player) {
        if (value == player)
        {
            MovePlayer(game).start()
        }
    }

    override fun run()
    {
        try {
            val move = getBestMove()
            handler.post {
                game.doMove(move)
                interrupt()
            }
        }
        catch (e: Exception)
        {
            val r = e.message
        }
    }

    private  fun getBestMove(): Move
    {
        val workingGame = this.game.copy()
        var maxRating: Int = Int.MIN_VALUE
        var bestMove = Move()
        val moves = workingGame.getAllMoves()
        moves.forEach {move ->
            workingGame.doMove(move)
            val rating = -this.rateBoard(workingGame, 6)
            if (rating > maxRating)
            {
                maxRating = rating
                bestMove = move
            }
            workingGame.unDoMove(move)
        }

        return bestMove
    }

    private fun rateBoard(workingGame: IGame, depth : Int = 8) : Int
    {
        if (depth == 0)
        {
            return workingGame.getRating()
        }
        var maxRating: Int = Int.MIN_VALUE
        val moves =  workingGame.getAllMoves()
        for (move in moves)
        {
            workingGame.doMove(move)
            val rating = -this.rateBoard(workingGame, depth - 1)
            if (rating > maxRating)
            {
                maxRating = rating
            }
            workingGame.unDoMove(move)
        }

        return maxRating
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

}

fun getIndex(pos: Pos) = pos.x / 2 + pos.y * 4

fun flipIndexes(board: ULong, vararg indexes: Int): ULong
{
    var out = board
    for (i in indexes)
    {
        out = out xor Math.pow(2.0, i.toDouble()).toULong()
    }

    return out
}