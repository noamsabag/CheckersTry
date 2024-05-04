package com.example.checkerstry.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs
import kotlin.properties.Delegates


interface IGame
{
    val turn : LiveData<Player>

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

class RegularGame(val size: Int): IGame
{
    //private var board: MutableList<MutableList<Piece?>> = mutableListOf<MutableList<Piece?>>()
    private val _turn : MutableLiveData<Player> = MutableLiveData(Player.White)
    override val turn : LiveData<Player>
        get() = _turn
    override var lastMove: Move? = null
    var moves = mutableListOf<Move>()
    var isMovesInitialized = false
    var board :Board = Board(size)
    object Constants
    {
        const val SIZE = 8
    }
    init
    {
        for (i in 0 until size)
        {
            val list: ArrayList<Piece?> = arrayListOf()
            for (j in 0 until size)
            {
                list.add(null)
            }
            board.add(list)
        }

        for (i in 0 until size * size)
        {
            if ((i / size + i % size) % 2 == 0)
            {
                if (i < size * 3) {
                    board[i / size][i % size] = Piece(Player.White, size)
                } else if (i >= size * 5) {
                    board[i / size][i % size] = Piece(Player.Black, size)
                }
            }
        }
    }

    override fun doMove(move: Move)
    {
        val steps = move.steps
        var pos = move.pos
        val move2 = move.copy()
        val endPos: Pos = move.steps[move.steps.count() - 1]
        while (move.steps.isNotEmpty())
        {
            val tar: Pos = move.steps[0]
            val direction = Pos(y= (tar.y - pos.y) / abs((tar.y - pos.y)), x= (tar.x - pos.x) / abs((tar.x - pos.x)))
            board[tar] = board[pos]
            board[pos.y][pos.x] = null
            while (pos.x != tar.x && pos.y != tar.y)
            {
                if (board[pos] != null)
                {
                    board[pos] = null
                }
                pos = pos + direction
            }

            move.steps.removeAt(0)
        }
        if (endPos.y == 0 && board[endPos]?._player == Player.Black)
        {
            board[endPos]?.Queen()
        }
        else if (endPos.y == size - 1 && board[endPos]?._player == Player.White)
        {
            board[endPos]?.Queen()
        }
        isMovesInitialized = false
        lastMove = move2
        _turn.value = _turn.value?.next()
    }

    fun basicGetMoves(pos: Pos, directions: List<Pos>? = null, isChain: Boolean = false): List<Move>
    {
        val piece = board[pos]
        val moves: MutableList<Move> = mutableListOf()
        val dirs = directions ?: this.getDirections(pos)
        for (direction in dirs)
        {
            var i = 1
            var opponentsPassed = 0
            var alliesPassed = 0
            while (!isChain && i <= piece!!._reach && board.isPosLegal(pos + direction * i) && opponentsPassed == 0 && alliesPassed == 0)
            {
                if (board[pos + direction * i] == null)
                {
                    moves.add(moveof(pos, pos + direction * i))
                }
                else if (board[pos+ direction * i]?._player == board[pos]?._player)
                {
                    alliesPassed++
                }
                else
                {
                    opponentsPassed++
                }
                i++
            }
            while (i <= piece!!._eatingReach && board.isPosLegal(pos + direction * i) && opponentsPassed <= 1 && alliesPassed == 0)
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
                    if (board[pos + direction * i]?._player == board[pos]?._player)
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
                if (board[pos] != null && board[pos]!!._player == turn.value)
                {
                    temp.addAll(basicGetMoves(pos))
                }
            }
            val maxMove = temp.maxBy { it -> it.eaten.keys.size }.eaten.keys.size
            temp.filter { it.eaten.keys.size >= maxMove }.forEach { moves.add(it)}
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
                if (board[i][j] != null && board[i][j]?._player == Player.White)
                {
                    white = true
                } else if (board[i][j] != null && board[i][j]?._player == Player.Black)
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
        if (piece != null && piece._isQueen)
        {
            directions = mutableListOf(Pos(1, 1), Pos(1, -1), Pos(-1, 1), Pos(-1, -1))
        }
        else if (piece != null)
        {
            if (piece._player == Player.Black)
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
        _turn.value = _turn.value?.previous()
    }

    override fun copy() : IGame
    {
        val out = RegularGame(size)
        out.board = this.board.copy()
        out._turn.value = this.turn.value
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
        var rating: Int = 0
        for (pos in this.getPoses())
        {
            if (board[pos] != null)
            {
                if (board[pos]!!._player == this.turn.value)
                {
                    rating += board[pos]!!.getRating()

                }
                else
                {
                    rating -= board[pos]!!.getRating()
                }
            }
        }
        return rating
    }

}