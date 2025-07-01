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
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

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


fun openDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selection
        val formattedDate = String.format("%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        onDateSelected(formattedDate)
    }

    if (context is AppCompatActivity) {
        datePicker.show(context.supportFragmentManager, datePicker.toString())
    } else {
        Toast.makeText(context, "Erro ao exibir o seletor de data", Toast.LENGTH_SHORT).show()
    }
}