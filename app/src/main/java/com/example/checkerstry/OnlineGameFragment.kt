package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.FragmentOnlineGameBinding

class OnlineGameFragment : Fragment() {

    private var _binding: FragmentOnlineGameBinding?  = null
    private  val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnlineGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUserName.text = "${UserData.userName}(${UserData.eloRanking})"
        binding.tvGamesPlayed.text = "Games Played: ${UserData.gamesPlayed}"
        val winRate = if (UserData.gamesPlayed != 0) UserData.gamesWon * 100 / UserData.gamesPlayed else 0
        binding.tvWinRate.text = "Win Rate: $winRate%"

        OnlinePlayersCounter.playerCount.observe(viewLifecycleOwner) {
            binding.tvPlayersCounter.text = "Players Online: ${it}"
        }

        GameData.isOnline = true

        binding.joinCreateRoom.setOnClickListener{
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }

        binding.quickMatchRegularGame.setOnClickListener{
            GameData.gameType = GameType.RegularGame
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }

        binding.quickMatchChessInTheDark.setOnClickListener{
            GameData.gameType = GameType.CheckersInTheDark
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }
    }


}