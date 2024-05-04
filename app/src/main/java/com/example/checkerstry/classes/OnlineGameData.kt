package com.example.checkerstry.classes

enum class GameState
{
    WaitingForPlayers,
    Ready,
    Ongoing,
    Finished
}

data class OnlinePlayerData(val tokenId: String, val name:String, val player: Player)

//data class OnlineGameData(val players: MutableMap<String, OnlinePlayerData?> = mutableMapOf(),var game:IGame, val gameId: String = "1", var gameState: GameState = GameState.Ongoing)

data class OnlineGameData(var movesDone: ArrayList<Move> = arrayListOf())