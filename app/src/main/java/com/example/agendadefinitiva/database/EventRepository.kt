package com.example.agendadefinitiva.database

import kotlinx.coroutines.flow.Flow

class EventRepository(private val dao: EventDAO) {
    val allEvents: Flow<List<EventEntity>> = dao.getAllEvents()

    suspend fun addEvent(event: EventEntity) {
        dao.insertEvent(event)
    }

    suspend fun removeEvent(event: EventEntity) {
        dao.deleteEvent(event)
    }
}
