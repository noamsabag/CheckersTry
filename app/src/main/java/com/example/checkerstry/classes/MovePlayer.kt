package com.example.checkerstry.classes

import android.os.Handler
import android.os.Looper

class MovePlayer(val game: IGame): Thread() {

    val handler = Handler(Looper.getMainLooper())
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
            workingGame.doMove(move.copy())
            val rating = this.rateBoard(workingGame, 5)
            if (rating > maxRating)
            {
                maxRating = rating
                bestMove = move
            }
            workingGame.unDoMove(move.copy())
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
            val rating = this.rateBoard(workingGame, depth - 1)
            if (rating > maxRating)
            {
                maxRating = rating
            }
            workingGame.unDoMove(move.copy())
        }

        return maxRating
    }
}
