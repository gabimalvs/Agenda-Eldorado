package com.example.agendadefinitiva

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.agendadefinitiva.database.AppDatabase
import com.example.agendadefinitiva.database.EventRepository
import com.example.agendadefinitiva.databinding.ActivityMainBinding
import com.example.agendadefinitiva.navGraph.NavGraph
import com.example.agendadefinitiva.viewmodel.EventViewModel

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        val repository = EventRepository(database.eventDao())

        val factory = EventViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        setContent {
            MaterialTheme {
                NavGraph()
            }
        }
        requestNotificationPermission()

    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }
    }
}