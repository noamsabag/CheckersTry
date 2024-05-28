package com.example.checkerstry.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.checkerstry.R

/**
 * An adapter for displaying a history of finished games in a list view. Each list item shows the outcome of a game
 * (win or loss) and the opponent's name along with their Elo ranking.
 *
 * @param context The current context. Used to inflate the layout file.
 * @param games An ArrayList of FinishedGameData objects containing information about each finished game.
 */
class GameHistoryAdapter(private val context: Context, private val games: ArrayList<FinishedGameData>) :
    ArrayAdapter<FinishedGameData>(context, R.layout.game_item, games) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.game_item, parent, false)

        // Find views to populate data.
        val ivGameResult = view.findViewById<ImageView>(R.id.ivGameResult)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)

        // Determine if the logged-in user is the winner to set appropriate icons and text.
        if (games[position].winner.userId == UserData.userId) {
            ivGameResult.setImageResource(R.drawable.win_icon)  // Show win icon.
            tvUserName.text = "${games[position].looser.userName}(${games[position].looser.eloRanking})"
        } else {
            ivGameResult.setImageResource(R.drawable.loss_icon)  // Show loss icon.
            tvUserName.text = "${games[position].winner.userName}(${games[position].winner.eloRanking})"
        }

        return view
    }
}
