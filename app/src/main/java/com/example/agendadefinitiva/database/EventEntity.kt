package com.example.agendadefinitiva.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val type: String,
    val people: String,
    val timeStart: Long,
    val timeEnd: Long,
    val status: String
)
