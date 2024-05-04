package com.example.checkerstry.classes

class Board(val _size:Int): ArrayList<ArrayList<Piece?>>()
{
    operator fun get(pos: Pos) = this[pos.y][pos.x]

    operator fun set(pos: Pos, piece: Piece?) {this[pos.y][pos.x] = piece}

    fun isPosLegal(pos: Pos): Boolean = pos.x in 0..<_size && pos.y in 0..<_size

    fun copy() : Board
    {
        throw NotImplementedError()
    }
}