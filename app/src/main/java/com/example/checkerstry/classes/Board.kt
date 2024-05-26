package com.example.checkerstry.classes

import java.math.BigInteger

// Shai: Prefer composition over inheritance.
// Shai: Use Array instead of ArrayList
class Board(val size:Int)
{
    val board = Array(size) {
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
                    output[i, j] = this[i, j]?.copy()
                }
            }
        }

        return output
    }
}

class LiteBoard(val pieces: ULong = 0xFFF0_0000_0000_0FFFu, val queens: UInt = 0x000_0000_0000_0000u)
{
    operator fun get(pos: Pos): Int = (((pieces shr pos.x + 4 * pos.y) and 1u - ((pieces shr (pos.x + 4 * pos.y + 32)) and 1u)) *
            (((queens shr pos.x + 4 * pos.y) and 1u) + 1u)).toInt()
}

class LBoard(val blackPieces: UInt = 0x0000_0FFFu, val whitePieces : UInt = 0xFFF0_0000u, val queens: UInt = 0x0000_0000u)
{
    operator fun get(pos: Pos): Int = (getBit(whitePieces, pos) - getBit(blackPieces, pos)) * (getBit(queens, pos) + 1)


}
fun getBit(num: UInt, pos: Pos): Int
{
    return (num shr (pos.x + 4 * pos.y) and 1u).toInt()
}


fun flipBit(num: UInt, pos: Pos) = num xor (1u shl (pos.x + 4 * pos.y - 1))

fun setBit(num: UInt, pos: Pos, res: Int) = if (getBit(num, pos) == res) num else flipBit(num, pos)

