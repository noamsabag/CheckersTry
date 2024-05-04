package com.example.checkerstry.classes

import android.R.attr
import android.content.Context
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import java.util.Dictionary


class CheckersButton(c: Context, private val pictures: Map<String, Int>, val _pos: Pos , var piece: Piece?): androidx.appcompat.widget.AppCompatImageButton(c)
{
    object Constants
    {
        const val SIZE = 127
    }
    var tileColor = Player.White
    var isTarget = false

    val pos: Pos = _pos
        get() = Pos(field)
    init
    {
        super.setLayoutParams(GridLayout.LayoutParams(ViewGroup.LayoutParams(Constants.SIZE, Constants.SIZE)))
        super.setPadding(0, 0, 0, 0)
        super.setScaleType(ImageView.ScaleType.CENTER_CROP)
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
        if ((pos.x + pos.y) % 2 == 0)
            if (tileColor == Player.White)
            {
                super.setImageResource(pictures["w"]!!);

            }
            else if (isTarget)
            {
                super.setImageResource(pictures["T"]!!);
            }
            else if (piece == null)
            {
                super.setImageResource(pictures["b"]!!);
            }
            else
            {
                super.setImageResource(pictures[piece.toString()]!!);
            }
    }
}