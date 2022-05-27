package com.example.coachtimer.ui.main.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.data.db.Player
import com.squareup.picasso.Picasso

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemPicture: ImageView
        var itemName: TextView

        init {
            itemPicture = itemView.findViewById(R.id.card_picture)
            itemName = itemView.findViewById(R.id.card_name)
        }
    }

    private var players: ArrayList<Player> = ArrayList()

    fun setData(list: ArrayList<Player>){
        players = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        Picasso.get().load(players[position].picture).into(holder.itemPicture)
        holder.itemName.text = players[position].name +" "+ players[position].surname
    }

    override fun getItemCount(): Int {
        return players.size
    }

}