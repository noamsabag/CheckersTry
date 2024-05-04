package com.example.checkerstry.classes

class CustomMap()
{
    val keys: ArrayList<Pos> = arrayListOf()
    val values: ArrayList<Piece> = arrayListOf()
    operator fun get(pos: Pos): Piece
    {
        if (pos in keys)
        {
            return values[keys.indexOf(pos)]
        }
        else
        {
            keys.add(pos)
            values.add(Piece(Player.White, 0))
            return values.last()
        }
    }

    fun put(pos: Pos, piece: Piece)
    {
        keys.add(pos)
        values.add(piece)
    }
}