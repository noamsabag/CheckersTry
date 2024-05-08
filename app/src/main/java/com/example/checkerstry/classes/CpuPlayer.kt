package com.example.checkerstry.classes

import android.os.Handler
import android.os.Looper

class CpuPlayer(var game: IGame, val player: Player) : MoveProvider, Thread()
{
    val handler = Handler(Looper.getMainLooper())

    override fun run()
    {
        try {
            game.doMove(getBestMove())
        }
        catch (e: Exception)
        {
            val r = e.message
        }
    }
    override fun onChanged(value: Player) {
        if (value == player)
        {
            start()
        }
    }

    private  fun getBestMove(): Move
    {
        val workingGame = this.game.copy()
        var maxRating: Int = Int.MIN_VALUE
        var bestMove = Move()
        val moves = workingGame!!.getAllMoves()
        moves.forEach {move ->
            workingGame!!.doMove(move.copy())
            val rating = -this.rateBoard(workingGame!!, 2)
            if (rating > maxRating)
            {
                maxRating = rating
                bestMove = move
            }
            workingGame!!.unDoMove(move.copy())
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
            workingGame.doMove(move.copy())
            val rating = -this.rateBoard(workingGame, depth - 1)
            if (rating > maxRating)
            {
                maxRating = rating
            }
            workingGame.unDoMove(move.copy())
        }

        return maxRating
    }
}