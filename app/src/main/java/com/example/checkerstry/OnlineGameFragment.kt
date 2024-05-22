package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.tvWinRate.text = "Win Rate: ${UserData.gamesWon / UserData.gamesPlayed * 100}%"

        binding.joinCreateRoom.setOnClickListener{
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }

        binding.quickMatch.setOnClickListener{
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }
    }


}