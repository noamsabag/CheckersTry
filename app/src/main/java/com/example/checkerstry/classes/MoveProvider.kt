package com.example.checkerstry.classes

import androidx.lifecycle.Observer


enum class MoveProvioderType
{
    ONLINE,
    CPU,
    VIEW
}

interface MoveProvider : Observer<Player> {

    override fun onChanged(value: Player)

}