package com.example.checkerstry.classes

class CpuPlayer(var game: IGame, val player: Player) : MoveProvider
{
    override fun onChanged(value: Player) {

    }

    private  fun getBestMove()
    {
        val workingGame = this.game.copy()
        var maxRating: Int = 0
        var bestMove = Move()
        for (pos in game.getPoses())
        {
            for (move in game.getMoves(pos))
            {
                game.doMove(move)
                val rating = this.rateBoard(game, 10)
                if (rating > maxRating)
                {
                    maxRating = rating
                    bestMove = move
                }
                game.unDoMove(move)
            }
        }

    }

    private fun rateBoard(game: IGame, depth : Int = 5) : Int
    {
        if (depth == 0)
        {
            return game.getRating()
        }
        var maxRating: Int = Int.MIN_VALUE
        for (pos in game.getPoses())
        {
            for (move in game.getMoves(pos))
            {
                game.doMove(move)
                val rating = -this.rateBoard(game, depth - 1)
                if (rating > maxRating)
                {
                    maxRating = rating
                }
                game.unDoMove(move)
            }
        }
        return maxRating
    }
}