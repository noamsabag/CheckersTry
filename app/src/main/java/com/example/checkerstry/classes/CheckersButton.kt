package com.example.checkerstry.classes

import android.content.Context
import android.view.ViewGroup
import android.widget.GridLayout


class CheckersButton(c: Context, private val pictures: Map<String, Int>, pos: Pos , var piece: Piece?, boardSize: Int): androidx.appcompat.widget.AppCompatImageButton(c)
{
    private val size = (127 * (8.0 / boardSize.toDouble())).toInt()
    private var tileColor = Player.White
    var isTarget = false
    var isNotDark = true

    val pos: Pos = pos
        get() = Pos(field)
    init
    {
        super.setLayoutParams(GridLayout.LayoutParams(ViewGroup.LayoutParams(size, size)))
        super.setPadding(0, 0, 0, 0)
        super.setScaleType(ScaleType.CENTER_CROP)
        if ((pos.x + pos.y) % 2 == 0)
        {
            tileColor = Player.Black
            super.setImageResource(pictures["b"]!!)
        } else {
            tileColor = Player.White
            super.setImageResource(pictures["w"]!!)
        }
    }

    fun updateImage()
    {
        if (!isNotDark)
        {
            super.setImageResource(pictures["d"]!!)
        }
        else if (tileColor == Player.White)
        {
            super.setImageResource(pictures["w"]!!)
        }
        else if (isTarget)
        {
            super.setImageResource(pictures["T"]!!)
        }
        else if (piece == null)
        {
            super.setImageResource(pictures["b"]!!)
        }
        else
        {
            super.setImageResource(pictures[piece.toString()]!!)
        }
    }
}