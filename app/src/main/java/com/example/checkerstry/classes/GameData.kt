package com.example.checkerstry.classes


enum class GameType
{
    RegularGame,
    CheckersInTheDark,
    SmallGame,
    BigGame
}

object GameTypeDictionary
{
    val dictionary = hashMapOf(
        "Regular Game" to GameType.RegularGame,
        "Checkers In The Dark" to GameType.CheckersInTheDark,
        "Small Game" to GameType.SmallGame,
        "Big Game" to GameType.BigGame
        )
}


object GameData
{
    var gameType: GameType = GameType.RegularGame
    var isOnline = false
    var myPlayer = Player.White
    var gameId = ""


}