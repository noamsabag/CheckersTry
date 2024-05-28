package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.checkerstry.classes.GameData
import com.example.checkerstry.classes.GameType
import com.example.checkerstry.databinding.FragmentMainMenuBinding

/**
 * Fragment representing the main menu of the application.
 * Provides options for starting different types of games or navigating to the profile page.
 */
class MainMenuFragment : Fragment() {

    // Binding property to handle view references
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners for menu options
        binding.quickMatchRegularGame.setOnClickListener {
            GameData.isOnline = true
            GameData.gameType = GameType.RegularGame
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }

        binding.quickMatchChessInTheDark.setOnClickListener {
            GameData.isOnline = true
            GameData.gameType = GameType.CheckersInTheDark
            val intent = Intent(context, MatchLoadingActivity::class.java)
            startActivity(intent)
        }

        binding.passAndPlayRegularGame.setOnClickListener {
            GameData.isOnline = false
            GameData.gameType = GameType.RegularGame
            val intent = Intent(context, GameActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
