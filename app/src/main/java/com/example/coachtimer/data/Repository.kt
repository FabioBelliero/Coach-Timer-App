package com.example.coachtimer.data

import com.example.coachtimer.data.db.Player
import com.example.coachtimer.data.db.PlayersDAO

class Repository(private val dao: PlayersDAO) {

    fun getPlayers(): MutableList<Player> {
        return dao.getAll()
    }

    suspend fun insertPlayer(player: Player){
        dao.insertPlayer(player)
    }

    suspend fun updatePlayer(player: Player){
        dao.updatePlayer(player)
    }

}