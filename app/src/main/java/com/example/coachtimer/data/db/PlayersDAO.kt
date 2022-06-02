package com.example.coachtimer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayersDAO {

    @Query("SELECT * FROM players")
    fun getAll(): MutableList<Player>

    @Insert
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)
}