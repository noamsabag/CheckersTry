package com.example.checkerstry.classes

/**
 * Custom implementation of a map that associates positions (Pos) with pieces (Piece).
 * This class manages keys and values in separate ArrayLists, allowing for specific custom behaviors.
 */
class CustomMap() {
    /**
     * An ArrayList of Pos objects representing the keys of the map.
     */
    val keys: ArrayList<Pos> = arrayListOf()

    /**
     * An ArrayList of Piece objects representing the values of the map.
     */
    val values: ArrayList<Piece> = arrayListOf()

    /**
     * Retrieves or adds a piece based on the given position. If the position exists as a key, it returns the associated piece.
     * If the position does not exist, it adds the position to the keys and a default piece (White, not a queen) to the values,
     * and then returns this new piece.
     *
     * @param pos The position key to retrieve or add a piece for.
     * @return The Piece associated with the given position.
     */
    operator fun get(pos: Pos): Piece {
        val index = keys.indexOf(pos)
        if (index != -1) {
            // If the key exists, return the corresponding piece.
            return values[index]
        } else {
            // If the key does not exist, add a default piece and return it.
            keys.add(pos)
            values.add(Piece(Player.White, false))
            return values.last()
        }
    }

    /**
     * Puts a piece at the specified position in the map. If the position already exists as a key,
     * it updates the existing piece; otherwise, it adds a new key-value pair.
     *
     * @param pos The position key where the piece is to be stored.
     * @param piece The piece to be associated with the specified position.
     */
    fun put(pos: Pos, piece: Piece) {
        val index = keys.indexOf(pos)
        if (index != -1) {
            // If the key exists, update the piece at the corresponding index.
            values[index] = piece
        } else {
            // If the key does not exist, add the new key-value pair.
            keys.add(pos)
            values.add(piece)
        }
    }
}
