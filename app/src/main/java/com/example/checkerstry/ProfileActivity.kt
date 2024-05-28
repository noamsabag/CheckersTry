package com.example.checkerstry

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.checkerstry.classes.FirebaseAuthHelper
import com.example.checkerstry.classes.UserData
import com.example.checkerstry.databinding.ActivityProfileBinding

/**
 * An activity that displays the user's profile, including statistics like username, Elo ranking,
 * number of games played, win rate, and also includes a fragment to show game history.
 */
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding  // Binding instance to access views.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display user information in the profile.
        binding.tvUserName.text = "${UserData.userName}(${UserData.eloRanking})"
        binding.tvGamesPlayed.text = "Games Played: ${UserData.gamesPlayed}"

        // Calculate and display the win rate as a percentage.
        val winRate = if (UserData.gamesPlayed != 0) (UserData.gamesWon * 100) / UserData.gamesPlayed else 0
        binding.tvWinRate.text = "Win Rate: $winRate%"

        // Load the GameHistoryFragment to display the user's game history.
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, GameHistoryFragment())
            commit()
        }

        // Setup a button that allows the user to delete their account.
        binding.btnDeleteAccount.setOnClickListener {
            deleteAccount()
        }
    }

    /**
     * Initiates the process of deleting the user's account. Shows a progress dialog during the deletion
     * process and redirects to the login page upon completion.
     */
    private fun deleteAccount() {
        val dialog = ProgressDialog(this).apply {
            setMessage("Deleting account")
            show()
        }

        // Call FirebaseAuthHelper to delete the account and handle the UI updates and redirection post-deletion.
        FirebaseAuthHelper.deleteAccount {
            dialog.cancel()  // Dismiss the progress dialog once deletion is complete.
            UserData.reset()  // Reset the user data stored in the application.
            startActivity(Intent(this, LoginPage::class.java))  // Redirect to the login page.
        }
    }
}
