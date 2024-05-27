package com.example.checkerstry.classes

object UserData
{
    var userId: String = "1"
    var userName: String = "User Name 1"
    var gamesPlayed: Int = 0
    var gamesWon: Int = 0
    var eloRanking: Int = 800
    var email = ""
    var password = ""


    fun reset()
    {
        userId = ""
        userName = ""
        gamesPlayed = 0
        gamesWon = 0
        eloRanking = 0
        email = ""
        password = ""
    }
}