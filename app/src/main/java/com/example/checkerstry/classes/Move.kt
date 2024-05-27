package com.example.checkerstry.classes


class Move(var pos: Pos = Pos())
{
    val eaten: CustomMap = CustomMap()
    val steps = mutableListOf<Pos>()
    var queen = false
    fun copy(): Move
    {
        val move = Move(this.pos)
        move.queen = this.queen
        this.steps.forEach { move.steps.add(it) }
        this.eaten.keys.forEach { move.eaten.keys.add(it)}
        this.eaten.values.forEach { move.eaten.values.add(it) }
        return move
    }
    override fun equals(other: Any?) : Boolean
    {
        if (other == null) return false
        if (other is Move)
        {
            val move = other as Move
            return this.pos == move.pos && this.steps.first() == move.steps.first()
        }
        return false
    }
}

fun moveof(pos: Pos, vararg steps: Pos): Move
{
    val move = Move(pos)
    steps.forEach { step -> move.steps.add(step) }
    return move
}