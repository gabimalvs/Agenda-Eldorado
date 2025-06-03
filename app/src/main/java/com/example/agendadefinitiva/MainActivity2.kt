package com.example.agendadefinitiva

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.agendadefinitiva.database.AppDatabase
import com.example.agendadefinitiva.database.EventRepository
import com.example.agendadefinitiva.navGraph.NavGraph
import com.example.agendadefinitiva.viewmodel.EventViewModel
import com.example.agendadefinitiva.viewmodel.EventViewModelFactory

class MainActivity2 : AppCompatActivity() {
    private lateinit var viewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        val repository = EventRepository(database.eventDao())

        val factory = EventViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        supportActionBar?.hide()
        setContent {
            MaterialTheme {
                NavGraph(viewModel)
            }
        }
    }
}