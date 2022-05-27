package com.example.coachtimer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Players")
class Player(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "surname") var surname: String,
    @ColumnInfo(name = "picture") var picture: String,
    @ColumnInfo(name = "explosiveness") var explosiveness: Double,
    @ColumnInfo(name = "endurance") var endurance: Int
)
{



}