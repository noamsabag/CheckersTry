package com.example.checkerstry.classes

import java.util.Dictionary

enum class GameType
{
    RegularGame,
    CheckersInTheDark,
    SmallGame
}

object GameTypeDictionary
{
    val dictionary = hashMapOf("Regular Game" to GameType.RegularGame,
                                "Checkers In The Dark" to GameType.CheckersInTheDark)
}


object GameData
{
    private var gameType: GameType = GameType.RegularGame
    private var moveProviders: HashMap<Player, MoveProvioderType> = hashMapOf()
    private var gameId = ""

    fun setGameId(string: String)
    {
        gameId = string
    }

    fun setGameType(gameType: GameType)
    {
        this.gameType = gameType
    }

    fun addMoveProovidor(player: Player, moveProvioderType: MoveProvioderType)
    {
        this.moveProviders[player] = moveProvioderType
    }

    fun getGameType() = gameType

    fun getMoveProviders() = moveProviders

    fun getGameId() = gameId

}