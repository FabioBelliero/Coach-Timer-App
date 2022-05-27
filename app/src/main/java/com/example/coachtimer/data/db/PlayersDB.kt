package com.example.coachtimer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1)
abstract class PlayersDB : RoomDatabase(){
    abstract fun playersDao(): PlayersDAO

    companion object{

        @Volatile
        private var INSTANCE: PlayersDB? = null

        fun getDB(context: Context): PlayersDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayersDB::class.java,
                    "players_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}