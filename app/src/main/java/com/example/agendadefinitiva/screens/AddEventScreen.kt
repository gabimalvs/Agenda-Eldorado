package com.example.agendadefinitiva.screens

import androidx.compose.foundation.lazy.LazyColumn
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import com.example.agendadefinitiva.fragments.CustomCalendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.agendadefinitiva.viewmodel.EventViewModel
import androidx.compose.runtime.collectAsState
import com.example.agendadefinitiva.database.EventEntity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.agendadefinitiva.openDatePicker
import java.text.SimpleDateFormat
import java.util.*
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.Toast
import com.example.agendadefinitiva.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController, eventViewModel: EventViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var people by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("To do") }

    var dateStart by remember { mutableStateOf("") }  // Data inicial
    var dateEnd by remember { mutableStateOf("") }    // Data final

    val events by eventViewModel.events.collectAsState()
    var isCalendarVisible by remember { mutableStateOf(false) } // Estado para controlar visibilidade do calendário

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                // UI Elements
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(12.dp))

                TextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, maxLines = 6)
                Spacer(modifier = Modifier.height(12.dp))

                TextField(value = type, onValueChange = { type = it }, label = { Text("Type") })
                Spacer(modifier = Modifier.height(12.dp))

                TextField(value = people, onValueChange = { people = it }, label = { Text("People") })
                Spacer(modifier = Modifier.height(12.dp))

                // StatusDropdown
                StatusDropdown(selectedStatus = status, onStatusSelected = { status = it })
                Spacer(modifier = Modifier.height(12.dp))

                // Botão para exibir o calendário
                Button(onClick = { isCalendarVisible = !isCalendarVisible }) {
                    Text("Select Event Date")
                }

                if (isCalendarVisible) {
                    AndroidView(
                        factory = { context ->
                            val themedContext = ContextThemeWrapper(context, R.style.CustomCalendarTheme)
                            CalendarView(themedContext).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )

                                setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    val date = sdf.parse(selectedDate)
                                    val timestamp = date?.time ?: 0L

                                    dateStart = selectedDate
                                    Toast.makeText(context, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
                                    isCalendarVisible = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )


                }
                Spacer(modifier = Modifier.height(12.dp))

                // Save Button
                Button(onClick = {
                    if (title.isNotBlank()) {
                        val startTimestamp = parseDateToTimestamp(dateStart)
                        val endTimestamp = parseDateToTimestamp(dateEnd)
                        val newEvent = EventEntity(
                            id = (events.maxOfOrNull { it.id } ?: 0) + 1,
                            title = title,
                            description = description,
                            type = type,
                            people = people,
                            status = status,
                            timeStart = startTimestamp,
                            timeEnd = endTimestamp
                        )
                        eventViewModel.addEvent(newEvent)
                        navController.popBackStack()
                    }
                }) {
                    Text("Save")
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(selectedStatus: String, onStatusSelected: (String) -> Unit) {
    val statusOptions = listOf("To do", "Development", "Done")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedStatus,
            onValueChange = {},
            label = { Text("Status") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Use a `forEach` properly to access the status string
            statusOptions.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(status)  // Pass the selected status
                        expanded = false
                    }
                )
            }
        }
    }
}



fun openTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
            onTimeSelected(formattedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    timePickerDialog.show()
}

fun parseDateToTimestamp(date: String): Long {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        parsedDate?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}

