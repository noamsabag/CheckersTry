package com.example.checkerstry.classes

enum class Player()
{
    White,
    Black;
    fun next(): Player { if (this == Player.White) { return Player.Black }; return Player.White }
    fun previous() : Player { if (this == Player.White) { return Player.Black }; return Player.White}
        override fun toString(): String { if (this == Player.White) { return "White" }; return "Black" }

}

class Piece(val _player: Player = Player.White, var _boardSize: Int = 8)
{
    var _isQueen: Boolean = false
    var _reach: Int = 1
    var _eatingReach: Int = 2

    fun Queen()
    {
        _isQueen = true
        _reach = _boardSize
        _eatingReach = _boardSize
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
        if (_isQueen)
        {
            return 2
        }
        return 1
    }

    fun copy(): Piece
    {
        return Piece(_player, _boardSize).also { if (this._isQueen) it.Queen() }
    }
}