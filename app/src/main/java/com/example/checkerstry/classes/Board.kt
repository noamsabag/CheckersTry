package com.example.checkerstry.classes

import java.math.BigInteger

// Shai: Prefer composition over inheritance.
// Shai: Use Array instead of ArrayList
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

class LiteBoard(val pieces: ULong = 0xFFF0_0000_0000_0FFFu, val queens: UInt = 0x000_0000_0000_0000u, val myTurn: Boolean = true)
{
    operator fun get(pos: Pos): Int = (((pieces shr pos.x + 4 * pos.y) and 1u - ((pieces shr (pos.x + 4 * pos.y + 32)) and 1u)) *
            (((queens shr pos.x + 4 * pos.y) and 1u) + 1u)).toInt()
}