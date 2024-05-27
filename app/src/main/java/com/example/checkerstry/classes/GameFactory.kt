package com.example.checkerstry.classes

import kotlin.reflect.KProperty

object GameFactory
{
    fun create(gameType: GameType,  onTurnChanged: (KProperty<*>, Player, Player) -> Unit = { _, _, _ ->}): Game
    {
        return when (gameType) {
            GameType.RegularGame -> Game(8, onTurnChanged)
            GameType.SmallGame -> Game(4, onTurnChanged)
            GameType.BigGame -> Game(12, onTurnChanged)
            GameType.CheckersInTheDark -> Game(8, onTurnChanged)
        }
    }
}