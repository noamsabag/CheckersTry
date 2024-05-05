package com.example.checkerstry.classes

enum class GameState
{
    WaitingForPlayers,
    Ready,
    Ongoing,
    Finished
}

data class FinishedGameData(val winner: User, val looser: User, val gameType: GameType, val gameId: String)

//data class OnlineGameData(val players: MutableMap<String, OnlinePlayerData?> = mutableMapOf(),var game:IGame, val gameId: String = "1", var gameState: GameState = GameState.Ongoing)

data class OnlineGameData(var movesDone: ArrayList<Move> = arrayListOf())