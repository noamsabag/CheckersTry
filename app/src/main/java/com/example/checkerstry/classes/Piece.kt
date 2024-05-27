package com.example.checkerstry.classes

enum class Player()
{
    White,
    Black;
    fun next() = if (this == White) Black else White
    fun previous() = next()
    override fun toString(): String { if (this == White) { return "White" }; return "Black" }

}

class Piece(val player: Player = Player.White, val isQueen: Boolean = false)
{
    override fun toString(): String
    {
        if (isQueen)
        {
            if (player == Player.White)
            {
                return "WQ"
            }
            return "BQ"
        }
        if (player == Player.Black)
        {
            return "B "
        }
        return "W "
    }

}