package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.classes.FinishedGameData
import com.example.checkerstry.classes.FirebaseUsersHelper
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameHistoryAdapter
import com.example.checkerstry.databinding.FragmentGameHistoryBinding

/**
 * A fragment to display the history of finished games.
 * Users can tap on any game in the list to view its detailed analysis in the AnalysisActivity.
 */
class GameHistoryFragment : Fragment() {
    // List of finished games, sourced from FirebaseUsersHelper.
    private val gamesList: ArrayList<FinishedGameData> = FirebaseUsersHelper.gamesPlayed

    // View binding for this fragment.
    private var _binding: FragmentGameHistoryBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Once the view is created, sets up the list adapter and click listeners for the items.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Making list items clickable and setting an adapter to handle the presentation of game data.
        binding.listView.isClickable = true
        binding.listView.adapter = GameHistoryAdapter(requireContext(), gamesList)

        // Set an item click listener to launch the AnalysisActivity for the selected game.
        // GameData.gameType is updated to reflect the selected game for analysis.
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity, AnalysisActivity::class.java)
            GameData.gameType = gamesList[position].gameType  // Set the game type for the next activity.
            intent.putExtra("GAME_ID", gamesList[position].gameId)  // Pass the selected game ID.
            startActivity(intent)  // Start the AnalysisActivity.
        }
    }

    /**
     * When the fragment is destroyed, nullify the binding to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
