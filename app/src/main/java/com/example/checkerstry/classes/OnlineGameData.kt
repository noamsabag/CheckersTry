package com.example.checkerstry.classes

enum class GameState
{
    WaitingForPlayers,
    Ready
}

data class FinishedGameData(val winner: User = User(), val looser: User = User(), var gameType: GameType = GameType.RegularGame, var gameId: String = "")

//data class OnlineGameData(val players: MutableMap<String, OnlinePlayerData?> = mutableMapOf(),var game:IGame, val gameId: String = "1", var gameState: GameState = GameState.Ongoing)

data class OnlineGameData(var movesDone: ArrayList<Move> = arrayListOf())