package com.example.checkerstry.classes

import android.content.Context

class DarkGameView(c: Context, players: List<Player>, boardSize: Int, pictures: Map<String, Int>, game: RegularGame): GameView(c, players, pictures, game) {

    override fun updateBoard()
    {
        val dark = Array(boardSize) {
           return@Array Array(boardSize) {
               return@Array false
           }
        }

        for (i in buttons.indices)
        {
            for (j in buttons[0].indices)
            {
                if (buttons[i][j].piece != null && buttons[i][j].piece!!.player == game.turn)
                {
                    dark[i][j] = true
                    lightSurrounding(dark, j, i)
                }
            }
        }

        for (move in game.getAllMoves())
        {
            for (pos in move.steps)
            {
                dark[pos.y][pos.x] = true
            }
        }

        for (i in buttons.indices)
        {
            for (j in buttons[0].indices)
            {
                buttons[i][j].isNotDark = dark[i][j]
            }
        }

        super.updateBoard()
    }

    fun lightSurrounding(mat: Array<Array<Boolean>>, x: Int, y: Int)
    {
        if (y - 1 >= 0)
        {
            mat[y - 1][x] = true
            if (x - 1 >= 0)
            {
                mat[y - 1][x - 1] = true
            }
            if (x + 1 < game.size)
            {
                mat[y - 1][x + 1] = true

            }
        }
        if (y + 1 < game.size)
        {
            mat[y + 1][x] = true
            if (x - 1 >= 0)
            {
                mat[y + 1][x - 1] = true
            }
            if (x + 1 < game.size)
            {
                mat[y + 1][x + 1] = true

            }
        }
        if (x + 1 < game.size)
        {
            mat[y][x + 1] = true
        }

        if (x - 1 >= 0)
        {
            mat[y][x - 1] = true
        }
    }
}

