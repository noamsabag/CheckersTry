package com.example.checkerstry.classes

data class Pos(val x: Int, val y: Int)
{
    constructor(pos: Pos): this(pos.x, pos.y) {}

    constructor(): this(0, 0)

    operator fun plus(b: Pos): Pos = Pos(this.x + b.x, this.y + b.y)

    operator fun minus(b: Pos): Pos = Pos(this.x - b.x, this.y - b.y)

    operator fun times(b: Int): Pos = Pos(this.x * b, this.y * b)



    override fun toString(): String = "(x: ${this.x}, y: ${this.y})"

}