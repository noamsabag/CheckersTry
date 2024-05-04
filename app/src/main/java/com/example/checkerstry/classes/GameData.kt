package com.example.checkerstry.classes

import java.util.Dictionary

enum class GameType
{
    RegularGame,
    SmallGame
}

object GameTypeDictionary
{
    val dictionary = hashMapOf("RegularGame" to GameType.RegularGame)
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