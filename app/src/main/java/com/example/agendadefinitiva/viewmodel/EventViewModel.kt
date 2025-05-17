package com.example.agendadefinitiva.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class EventViewModel : ViewModel() {
    val events: SnapshotStateList<Event> = mutableStateListOf()

    fun addEvent(event: Event) {
        events.add(event)
    }
}