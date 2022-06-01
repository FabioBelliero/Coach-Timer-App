package com.example.coachtimer.ui.main.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.data.db.Player
import com.example.coachtimer.ui.main.utils.Lap


class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemLapNumber : TextView
        var itemLapTime : TextView
        var itemLapSpeed : TextView

        init {
            itemLapNumber = itemView.findViewById(R.id.lap_number)
            itemLapTime = itemView.findViewById(R.id.lap_time)
            itemLapSpeed = itemView.findViewById(R.id.lap_speed)
        }
    }

    private var lapList: ArrayList<Lap> = ArrayList()

    fun setData(list: ArrayList<Lap>){
        lapList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lap_layout, parent, false)
        return SessionViewHolder(v)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.itemLapNumber.text = lapList[position].n.toString()

        var time = lapList[position].time
        var mil : Int = (time.mod(1.0)*1000).toInt()
        var min : Int = (time/60).toInt()
        var sec : Int = time.mod(60.0).toInt()

        holder.itemLapTime.text = "Time: $min:$sec:$mil"
        holder.itemLapSpeed.text = "Speed: " + String.format("%.3f", lapList[position].avg_speed) + " m/s"

    }

    override fun getItemCount(): Int {
        return lapList.size
    }
}