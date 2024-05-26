package com.example.checkerstry.classes

import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Looper
import android.util.Log

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
        var bestRating: Int = 0
        var bestMove = Move()
        val moves = workingGame.getAllMoves()
        if (workingGame.turn == Player.White)
        {
            bestRating = Int.MIN_VALUE
            for (move in moves)
            {
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, 6)
                Log.i("shit", rating.toString())
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
            for (move in moves)
            {
                workingGame.doMove(move)
                val rating = this.rateBoard(workingGame, 6)
                Log.i("shit", rating.toString())
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
                val rating = this.rateBoard(workingGame, depth - 1, _alpha, _beta)
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
                val rating = this.rateBoard(workingGame, depth - 1, _alpha, _beta)
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


