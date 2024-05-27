package com.example.checkerstry.classes

import android.content.Context
import android.view.View
import android.widget.GridLayout
import androidx.lifecycle.Observer

open class GameView(c: Context, val players: List<Player>, pictures: Map<String, Int>, val game: Game): GridLayout(c), View.OnClickListener, Observer<Player>
{
    val boardSize = game.size
    var buttons: List<List<CheckersButton>>
    private var selectedPiece: CheckersButton? = null
    init
    {

        super.setRowCount(boardSize)
        super.setColumnCount(boardSize)
        val temp = mutableListOf<List<CheckersButton>>()
        for (i in 0 until boardSize)
        {
            val row = mutableListOf<CheckersButton>()
            for (j in 0 until boardSize)
            {
                val newBtn = CheckersButton(c, pictures, Pos(j, boardSize - 1 - i), game.board[Pos(j, boardSize - 1 - i)], boardSize)
                newBtn.setOnClickListener(this)
                row.add(newBtn)
                this.addView(newBtn)
            }
            temp.add(0, row.toList())
        }
        buttons = temp.toList()
        updateBoard()
    }

    override fun onChanged(value: Player) { updateBoard()}

    protected open fun updateBoard()
    {
        for (i in buttons.indices)
        {
            for (j in buttons[0].indices)
            {
                buttons[i][j].piece = game.board[i, j]
                buttons[i][j].isTarget = false
                buttons[i][j].updateImage()
            }
        }
    }

    override fun onClick(v: View?)
    {
        if (v is CheckersButton)
        {
            val btn: CheckersButton = v
            if (btn.piece?.player in players && btn.piece?.player == game.turn)
            {
                this.updateBoard()
                val moves = this.game.getMoves(btn.pos)
                for (move in moves)
                {
                    val lastMove = move.steps.last()
                    buttons[lastMove.y][lastMove.x].isTarget = true
                    buttons[lastMove.y][lastMove.x].updateImage()
                }
                selectedPiece = btn
            }
            else if (btn.isTarget)
            {
                var m = Move(selectedPiece!!.pos)
                for (move in this.game.getMoves(selectedPiece!!.pos))
                {
                    if (move.steps.last() == btn.pos)
                    {
                        m = move
                    }
                }
                this.game.doMove(m)
                selectedPiece = null
                this.updateBoard()
            }
        }
    }
}