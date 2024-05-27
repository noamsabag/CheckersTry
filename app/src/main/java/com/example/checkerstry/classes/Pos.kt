package com.example.checkerstry.classes

/**
 * Data class representing a position in a 2D space with x and y coordinates.
 * @param x the x-coordinate of the position.
 * @param y the y-coordinate of the position.
 */
data class Pos(val x: Int, val y: Int) {

    /**
     * Secondary constructor which creates a new position instance by copying coordinates from another position instance.
     * @param pos another Pos instance from which to copy coordinates.
     */
    constructor(pos: Pos): this(pos.x, pos.y)

    /**
     * Default constructor which initializes the position to the origin (0, 0).
     */
    constructor(): this(0, 0)

    /**
     * Overloads the plus operator to add two Pos instances.
     * Returns a new Pos instance where each coordinate is the sum of the corresponding coordinates of the two Pos instances.
     * @param b another Pos instance to add to this instance.
     * @return a new Pos instance resulting from the addition.
     */
    operator fun plus(b: Pos): Pos = Pos(this.x + b.x, this.y + b.y)

    /**
     * Overloads the minus operator to subtract one Pos instance from another.
     * Returns a new Pos instance where each coordinate is the difference of the corresponding coordinates of the two Pos instances.
     * @param b another Pos instance to subtract from this instance.
     * @return a new Pos instance resulting from the subtraction.
     */
    operator fun minus(b: Pos): Pos = Pos(this.x - b.x, this.y - b.y)

    /**
     * Overloads the times operator to multiply the coordinates of the Pos instance by an integer.
     * Returns a new Pos instance where each coordinate is the product of the original coordinate and the integer.
     * @param b the multiplier for both coordinates.
     * @return a new Pos instance resulting from the multiplication.
     */
    operator fun times(b: Int): Pos = Pos(this.x * b, this.y * b)

    /**
     * Provides a string representation of the Pos instance in the format (x: x-coordinate, y: y-coordinate).
     * @return a string that represents the coordinates of the Pos instance.
     */
    override fun toString(): String = "(x: ${this.x}, y: ${this.y})"
}
