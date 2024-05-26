package com.example.checkerstry.classes

import kotlin.reflect.KProperty

object GameFactory
{
    fun create(gameType: GameType,  onTurnChanged: (KProperty<*>, Player, Player) -> Unit = { _, _, _ ->}): IGame
    {
        return when (gameType) {
            GameType.RegularGame -> RegularGame(8, onTurnChanged)
            GameType.SmallGame -> RegularGame(4, onTurnChanged)
            GameType.BigGame -> RegularGame(12, onTurnChanged)
            GameType.CheckersInTheDark -> RegularGame(8, onTurnChanged)
        }
    }
}