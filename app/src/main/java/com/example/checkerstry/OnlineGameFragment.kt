package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.FragmentOnlineGameBinding

/**
 * Fragment to handle online game interactions, displaying user statistics and providing options
 * to join or create game rooms, and initiate quick matches.
 */
class OnlineGameFragment : Fragment() {

    // Binding object to access the fragment's views.
    private var _binding: FragmentOnlineGameBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the layout for this fragment and initializes the view binding.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnlineGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view is created, setting up UI elements and event listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display user name and Elo ranking.
        binding.tvUserName.text = "${UserData.userName}(${UserData.eloRanking})"

        // Display total games played.
        binding.tvGamesPlayed.text = "Games Played: ${UserData.gamesPlayed}"

        // Calculate and display win rate.
        val winRate = if (UserData.gamesPlayed != 0) UserData.gamesWon * 100 / UserData.gamesPlayed else 0
        binding.tvWinRate.text = "Win Rate: $winRate%"

        // Observe and display the count of online players.
        OnlinePlayersCounter.playerCount.observe(viewLifecycleOwner) {
            binding.tvPlayersCounter.text = "Players Online: $it"
        }

        // Ensure the GameData flags this session as an online game.
        GameData.isOnline = true

        // Set up button to join or create a room.
        binding.joinCreateRoom.setOnClickListener {
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }

        // Set up button for quick match in Regular Game mode.
        binding.quickMatchRegularGame.setOnClickListener {
            GameData.gameType = GameType.RegularGame
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }

        // Set up button for quick match in Checkers In The Dark mode.
        binding.quickMatchChessInTheDark.setOnClickListener {
            GameData.gameType = GameType.CheckersInTheDark
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Clean up the binding when the view is destroyed to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
