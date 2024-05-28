package com.example.checkerstry.classes

/**
 * Represents a move in a board game, encapsulating the starting position, the steps taken, captured pieces, and any change in piece status (e.g., promotion to queen).
 *
 * @param pos The starting position of the move. Defaults to the origin (0, 0) if not specified.
 */
class Move(var pos: Pos = Pos()) {
    /**
     * A map of positions to the pieces that were captured ('eaten') at those positions during the move.
     */
    val eaten: CustomMap = CustomMap()

    /**
     * A list of positions representing the sequential steps taken during the move.
     */
    val steps = mutableListOf<Pos>()

    /**
     * Flag indicating whether a piece involved in the move was promoted to a queen.
     */
    var queen = false

    /**
     * Creates a deep copy of this Move instance, duplicating its current state.
     * @return A new Move instance that is a copy of this move, including all steps and captures.
     */
    fun copy(): Move {
        val move = Move(this.pos)
        move.queen = this.queen
        this.steps.forEach { move.steps.add(it) }
        this.eaten.keys.forEach { move.eaten.keys.add(it) }
        this.eaten.values.forEach { move.eaten.values.add(it) }
        return move
    }

    /**
     * Checks if another object is equal to this Move instance.
     * Equality is defined as having the same starting position and the first step identical.
     *
     * @param other The object to compare with this instance.
     * @return True if the other object is a Move and has the same starting position and first step, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Move) {
            return this.pos == other.pos && this.steps.first() == other.steps.first()
        }
        return false
    }
}

/**
 * Factory function to create a Move instance with a specified starting position and a sequence of steps.
 *
 * @param pos The initial position of the move.
 * @param steps A variable number of position arguments representing the sequential steps of the move.
 * @return A Move instance initialized with the given position and steps.
 */
fun moveof(pos: Pos, vararg steps: Pos): Move {
    val move = Move(pos)
    steps.forEach { step -> move.steps.add(step) }
    return move
}
