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
class Piece(val _player: Player = Player.White, var _boardSize: Int = 8)
{
    var _isQueen: Boolean = false  // Shai: Make private
    var _reach: Int = 1 // Shai: This can be a function
    var _eatingReach: Int = 2

    fun Queen()
    {
        _isQueen = true
        _reach = _boardSize
        _eatingReach = _boardSize
    }

    fun unQueen()
    {
        _isQueen = false
        _reach = 1
        _eatingReach = 2
    }

    override fun toString(): String
    {
        if (_isQueen)
        {
            if (_player == Player.White)
            {
                return "WQ";
            }
            return "BQ";
        }
        if (_player == Player.Black)
        {
            return "B ";
        }
        return "W ";
    }

    fun getRating() : Int
    {
        if (_player == Player.Black)
        {
            if (_isQueen)
            {
                return -5
            }
            return -1
        }
        if (_isQueen)
        {
            return 5
        }
        return 1
    }

    fun copy(): Piece
    {
        return Piece(_player, _boardSize).also { if (this._isQueen) it.Queen() }
    }
}