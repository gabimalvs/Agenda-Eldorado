package com.example.agendadefinitiva.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendadefinitiva.database.EventEntity
import com.example.agendadefinitiva.database.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<EventEntity>>(emptyList())
    val events: StateFlow<List<EventEntity>> = _events

    init {
        viewModelScope.launch {
            repository.allEvents.collectLatest {
                _events.value = it
            }
        }
    }

    fun addEvent(title: String, description: String) {
        viewModelScope.launch {
            repository.addEvent(EventEntity(title = title, description = description))
        }
    }
}