package com.example.checkerstry.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


interface IGame
{
    var turn : Player

    var lastMove: Move?

    fun doMove(move: Move)

    fun getMoves(pos: Pos): List<Move>

    fun getAllMoves(): List<Move>

    fun getPoses() : List<Pos>

    fun isFinal(): Player?

    fun getRating(): Int

    fun getDirections(pos: Pos): List<Pos>

    fun unDoMove(move: Move)

    fun copy() : IGame

}

class RegularGame(val size: Int, onTurnChanged: (KProperty<*>, Player, Player) -> Unit = { _, _, _ ->}): IGame
{
    //private var board: MutableList<MutableList<Piece?>> = mutableListOf<MutableList<Piece?>>()
    override var turn : Player by Delegates.observable(Player.White) {  property, oldValue, newValue -> onTurnChanged(property, oldValue, newValue)}
    //private val _turn : MutableLiveData<Player> = MutableLiveData(Player.White)
    override var lastMove: Move? = null
    var moves = mutableListOf<Move>()
    var isMovesInitialized = false
    var board :Board = Board(size)
    private var rating = 0
    object Constants
    {
        const val SIZE = 8
    }

    override fun doMove(move: Move)
    {
        val saver = board[move.pos]
        board[move.pos] = null
        board[move.steps.last()] = saver

        for (key in move.eaten.keys) {
            board[key] = null
        }
        val move2 = move.copy()
        val endPos: Pos = move.steps.last()
        move.eaten.values.forEach {
            rating -= it.getRating()
        }

        if (move.queen)
        {
            board[endPos] = Piece(board[endPos]!!.player, true)
        }

        isMovesInitialized = false
        moves = mutableListOf()
        lastMove = move2
        turn = turn.next()

    }

    fun basicGetMoves(pos: Pos, directions: List<Pos>? = null, isChain: Boolean = false): List<Move>
    {
        val piece = board[pos]
        if (piece == null) return listOf()
        val moves: MutableList<Move> = mutableListOf()
        val dirs = directions ?: this.getDirections(pos)
        var reach = 1
        var eatingReach = 2
        if (piece.isQueen)
        {
            reach = size
            eatingReach = size
        }
        for (direction in dirs)
        {
            var i = 1
            var opponentsPassed = 0
            var alliesPassed = 0
            while (!isChain && i <= reach && board.isPosLegal(pos + direction * i) && opponentsPassed == 0 && alliesPassed == 0)
            {
                if (board[pos + direction * i] == null)
                {
                    moves.add(moveof(pos, pos + direction * i))
                }
                else if (board[pos+ direction * i]?.player == piece.player)
                {
                    alliesPassed++
                }
                else
                {
                    opponentsPassed++
                }
                i++
            }
            while (i <= eatingReach && board.isPosLegal(pos + direction * i) && opponentsPassed <= 1 && alliesPassed == 0)
            {
                if (board[pos + direction * i] == null && opponentsPassed == 1)
                {
                    board[pos + direction * i] = board[pos]
                    board[pos] = null
                    var saver: Piece? = null
                    var changedAt = 1
                    for (j in 1 until i)
                    {
                        if (board[pos + direction * j] != null)
                        {
                            saver = board[pos + direction * j]
                            board[pos + direction * j] = null
                            changedAt = j
                            break
                        }
                    }
                    val newMoves = this.basicGetMoves(pos + direction * i, dirs, true)
                    newMoves.forEach{it.steps.add(0, it.pos); it.pos = pos + direction * i; it.eaten.put(pos + direction * changedAt, saver!!)}
                    moves.addAll(newMoves)
                    board[pos + direction * changedAt] = saver
                    board[pos] = board[pos + direction * i]
                    board[pos + direction * i] = null
                }
                else if (board[pos + direction * i] != null)
                {
                    if (board[pos + direction * i]?.player == board[pos]?.player)
                    {
                        alliesPassed++
                    }
                    else
                    {
                        opponentsPassed++
                    }
                }
                i++
            }


        }
        if (isChain)
        {
            val m = Move(pos)
            moves.add(m)
        }
        moves.forEach { it.pos = Pos(pos) }
        return moves
    }

    fun initMoves()
    {
        if (!isMovesInitialized)
        {
            moves.clear()
            val temp = mutableListOf<Move>()
            for (pos in getPoses())
            {
                if (board[pos] != null && board[pos]!!.player == turn)
                {
                    temp.addAll(basicGetMoves(pos))
                }
            }
            val maxMove = temp.maxBy { it.eaten.keys.size }.eaten.keys.size
            temp.filter { it.eaten.keys.size >= maxMove }.forEach {
                val endPos = it.steps.last()
                if (endPos.y == 0 && board[it.pos]?.player == Player.Black)
                {
                    it.queen = true
                }
                else if (endPos.y == size - 1 && board[it.pos]?.player == Player.White)
                {
                    it.queen = true
                }
                moves.add(it)
            }
            isMovesInitialized = true
        }
    }

    override fun getMoves(pos: Pos): List<Move>
    {
        initMoves()
        val res = mutableListOf<Move>()
        moves.filterTo(res) { it.pos == pos }
        return res
    }

    override fun getAllMoves(): List<Move>
    {
        initMoves()
        return moves
    }

    override fun isFinal(): Player?
    {
        var white = false
        var black = false
        for (i in 0 until size)
        {
            for (j in 0 until size)
            {
                if (board[i, j] != null && board[i, j]?.player == Player.White)
                {
                    white = true
                } else if (board[i, j] != null && board[i, j]?.player == Player.Black)
                {
                    black = true
                }
            }
        }
        if (white && black) { return null }
        if (white) { return Player.White }
        return Player.Black
    }

    override fun getDirections(pos: Pos): List<Pos>
    {
        var directions = mutableListOf<Pos>()
        val piece = board[pos]
        if (piece != null && piece.isQueen)
        {
            directions = mutableListOf(Pos(1, 1), Pos(1, -1), Pos(-1, 1), Pos(-1, -1))
        }
        else if (piece != null)
        {
            if (piece.player == Player.Black)
            {
                directions = mutableListOf(Pos(-1, -1), Pos(1, -1))
            }
            else
            {
                directions = mutableListOf(Pos(-1, 1), Pos(1, 1))
            }
        }
        return directions.toList()
    }

    override fun unDoMove(move: Move)
    {
        board[move.pos] = board[move.steps.last()]
        board[move.steps.last()] = null
        for (pos in move.eaten.keys)
        {
            board[pos] = move.eaten[pos]
        }
        move.eaten.values.forEach {
            rating += it.getRating()
        }

        if (move.queen)
        {
            board[move.steps.last()] = Piece(board[move.steps.last()]!!.player, false)
        }
        turn = turn.previous()
    }

    override fun copy() : IGame
    {
        val out = RegularGame(size)
        out.board = this.board.copy()
        out.turn = this.turn
        out.lastMove = this.lastMove
        return out
    }

    override fun getPoses() : List<Pos>
    {
        val poses = mutableListOf<Pos>()
        for (i in 0 until size)
        {
            for (j in 0 until size)
            {
                poses.add(Pos(i, j))
            }
        }
        return poses
    }

    override fun getRating(): Int
    {
        var sum = 0

        for (pos in getPoses())
        {
            if (board[pos] != null)
            {
                sum += board[pos]!!.getRating()
            }
        }
        return sum
    }

}