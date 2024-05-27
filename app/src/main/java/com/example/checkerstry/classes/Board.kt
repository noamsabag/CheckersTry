package com.example.checkerstry.classes


class Board(val size:Int)
{
    private val board = Array(size) {
        return@Array Array<Piece?>(size) {
            return@Array null
        }
    }
    init
    {
        for (i in 0 until size * size)
        {
            if ((i / size + i % size) % 2 == 0)
            {
                if (i < size * (size / 2 - 1)) {
                    board[i / size][i % size] = Piece(Player.White, false)
                } else if (i >= size * (size / 2 + 1)) {
                    board[i / size][i % size] = Piece(Player.Black, false)
                }
            }
        }
    }
    operator fun get(y: Int, x: Int) = board[y][x]
    operator fun get(pos: Pos) = board[pos.y][pos.x]
    operator fun set(y: Int, x: Int, piece: Piece?) { board[y][x] = piece}
    operator fun set(pos: Pos, piece: Piece?) {board[pos.y][pos.x] = piece}

    fun isPosLegal(pos: Pos): Boolean = pos.x in 0..<size && pos.y in 0..<size

    fun copy() : Board
    {
        val output = Board(size)

        for (i in 0 until size)
        {
            for (j in 0 until size)
            {
                output[i, j] = null
                if (this[i, j] != null)
                {
                    output[i, j] = this[i, j]
                }
            }
        }

        return output
    }
}
