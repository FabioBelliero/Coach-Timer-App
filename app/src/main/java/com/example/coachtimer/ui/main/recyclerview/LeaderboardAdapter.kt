package com.example.coachtimer.ui.main.recyclerview

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.data.db.Player
import com.squareup.picasso.Picasso

class LeaderboardAdapter() : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    inner class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemPicture: ImageView
        var itemName: TextView
        var itemExpl: TextView
        var itemEnd: TextView

        init {
            itemPicture = itemView.findViewById(R.id.leader_picture)
            itemName = itemView.findViewById(R.id.leader_name)
            itemExpl = itemView.findViewById(R.id.leader_expl)
            itemEnd = itemView.findViewById(R.id.leader_end)
        }
    }

    private var players: ArrayList<Player> = ArrayList()
    private var expl = true

    fun setData(list: ArrayList<Player>){
        players = list
    }

    fun setSort(v: Boolean){
        expl = v
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_layout, parent, false)
        return LeaderboardViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        //Used Picasso to use the Url
        Picasso.get().load(players[position].picture).into(holder.itemPicture)
        holder.itemName.text = players[position].name +" "+ players[position].surname
        holder.itemExpl.text = String.format("%.3f",players[position].explosiveness) + " m/s"
        holder.itemEnd.text = "${players[position].endurance} laps"

        if(expl){
            holder.itemExpl.setTypeface(null, Typeface.BOLD)
            holder.itemEnd.setTypeface(null, Typeface.NORMAL)
        } else {
            holder.itemExpl.setTypeface(null, Typeface.NORMAL)
            holder.itemEnd.setTypeface(null, Typeface.BOLD)
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }
}