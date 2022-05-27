package com.example.coachtimer.data

import android.app.Application
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.coachtimer.data.db.Player
import com.example.coachtimer.data.db.PlayersDAO


class Repository(private val dao: PlayersDAO) {

    fun getPlayers(): List<Player> {
        return dao.getAll()
    }

    suspend fun insertPlayer(player: Player){
        dao.insertPlayer(player)
    }

    suspend fun updatePlayer(player: Player){
        dao.updatePlayer(player)
    }

}