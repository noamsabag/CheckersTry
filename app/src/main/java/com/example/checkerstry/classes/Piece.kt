package com.example.checkerstry.classes

enum class Player()
{
    White,
    Black;
    fun next(): Player {
        return if (this == Player.White) Player.Black else Player.White
    }
    fun previous() : Player { if (this == Player.White) { return Player.Black }; return Player.White}
        override fun toString(): String { if (this == Player.White) { return "White" }; return "Black" }

}

// Shai: Make Piece immutable
class Piece(val player: Player = Player.White, val isQueen: Boolean = false)
{
    override fun toString(): String
    {
        if (isQueen)
        {
            if (player == Player.White)
            {
                return "WQ";
            }
            return "BQ";
        }
        if (player == Player.Black)
        {
            return "B ";
        }
        return "W ";
    }

    fun getRating() : Int
    {
        if (player == Player.Black)
        {
            if (isQueen)
            {
                return -5
            }
            return -1
        }
        if (isQueen)
        {
            return 5
        }
        return 1
    }

    fun copy(): Piece
    {
        return Piece(player, isQueen)
    }
}