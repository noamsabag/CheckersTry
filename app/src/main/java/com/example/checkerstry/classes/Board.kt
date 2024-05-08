package com.example.checkerstry.classes

class Board(val _size:Int): ArrayList<ArrayList<Piece?>>()
{
    operator fun get(pos: Pos) = this[pos.y][pos.x]

    operator fun set(pos: Pos, piece: Piece?) {this[pos.y][pos.x] = piece}

    fun isPosLegal(pos: Pos): Boolean = pos.x in 0..<_size && pos.y in 0..<_size

    fun copy() : Board
    {
        val output = Board(_size)
        for (i in 0 until size)
        {
            val list: ArrayList<Piece?> = arrayListOf()
            for (j in 0 until size)
            {
                list.add(null)
            }
            output.add(list)
        }

        for (i in 0 until _size)
        {
            for (j in 0 until _size)
            {
                output[i][j] = null
                if (this[i][j] != null)
                {
                    output[i][j] = this[i][j]?.copy()
                }
            }
        }

        return output
    }
}