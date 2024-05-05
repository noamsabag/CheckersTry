package com.example.checkerstry.classes

import android.app.Activity
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.checkerstry.R

class GameHistoryAdapter(private val context: Activity, private val games: ArrayList<FinishedGameData>): ArrayAdapter<FinishedGameData>(context, R.layout.game_item, games) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.game_item, null)

        val ivGameResult = view.findViewById<ImageView>(R.id.ivGameResult)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)

        if (games[position].winner.userId == com.example.checkerstry.classes.UserData.userId)
        {
            ivGameResult.setImageResource(R.drawable.win_icon)
            tvUserName.text = "${games[position].looser.userId}(${games[position].looser.eloRanking})"
        }
        else
        {
            ivGameResult.setImageResource(R.drawable.loss_icon)
            tvUserName.text = "${games[position].winner.userId}(${games[position].winner.eloRanking})"
        }

        return super.getView(position, convertView, parent)
    }
}