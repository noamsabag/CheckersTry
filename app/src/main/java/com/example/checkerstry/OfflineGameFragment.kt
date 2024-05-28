package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.databinding.FragmentOfflineGameBinding

/**
 * Fragment for selecting different types of offline games.
 * This fragment allows users to choose between various game modes for offline play.
 */
class OfflineGameFragment : Fragment() {

    // Binding property to handle view references
    private var _binding: FragmentOfflineGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOfflineGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the game mode to offline
        GameData.isOnline = false

        // Set up click listeners for each game type card, navigating to the game activity with the selected game type
        binding.cvBigGame.setOnClickListener {
            GameData.gameType = GameType.BigGame
            navigateToGameActivity()
        }

        binding.cvRegularGame.setOnClickListener {
            GameData.gameType = GameType.RegularGame
            navigateToGameActivity()
        }

        binding.cvSmallGame.setOnClickListener {
            GameData.gameType = GameType.SmallGame
            navigateToGameActivity()
        }
    }

    /**
     * Starts the GameActivity with the current game type specified in GameData.
     */
    private fun navigateToGameActivity() {
        val intent = Intent(context, GameActivity::class.java)
        startActivity(intent)
    }
}
