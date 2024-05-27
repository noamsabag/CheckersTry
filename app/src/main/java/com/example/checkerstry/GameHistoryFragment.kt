package com.example.checkerstry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.classes.FinishedGameData
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GameHistoryAdapter
import com.example.checkerstry.databinding.FragmentGameHistoryBinding

class GameHistoryFragment : Fragment() {
    val gamesList: ArrayList<FinishedGameData> = FirebaseUsersHelper.gamesPlayed
    private var _binding: FragmentGameHistoryBinding?  = null
    private  val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listView.isClickable = true
        binding.listView.adapter = GameHistoryAdapter(requireContext(), gamesList)
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this.activity, AnalysisActivity::class.java)
            intent.putExtra("GAME_ID", gamesList[position].gameId)
            startActivity(intent)
        }
    }
}