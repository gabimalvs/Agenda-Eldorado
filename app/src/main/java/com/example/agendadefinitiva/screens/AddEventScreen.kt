package com.example.agendadefinitiva.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import com.example.agendadefinitiva.viewmodel.EventViewModel
import androidx.compose.runtime.collectAsState
import com.example.agendadefinitiva.database.EventEntity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController, eventViewModel: EventViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var people by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("To do") }
    var dateStart by remember { mutableStateOf("") }  // ex: "03/06/2025"
    var timeStart by remember { mutableStateOf("") }  // ex: "14:30"
    var dateEnd by remember { mutableStateOf("") }
    var timeEnd by remember { mutableStateOf("") }

    StatusDropdown(selectedStatus = status, onStatusSelected = { status = it })
    val events by eventViewModel.events.collectAsState()
    val scrollState = rememberScrollState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 6
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = people,
                onValueChange = { people = it },
                label = { Text("People") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            StatusDropdown(selectedStatus = status, onStatusSelected = { status = it })
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = dateStart,
                onValueChange = { dateStart = it },
                label = { Text("Start Date (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = timeStart,
                onValueChange = { timeStart = it },
                label = { Text("Start Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = dateEnd,
                onValueChange = { dateEnd = it },
                label = { Text("End Date (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = timeEnd,
                onValueChange = { timeEnd = it },
                label = { Text("End Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val startTimestamp = parseDateTimeToTimestamp(dateStart, timeStart)
                        val endTimestamp = parseDateTimeToTimestamp(dateEnd, timeEnd)
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
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
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
            statusOptions.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun parseDateTimeToTimestamp(date: String, time: String): Long {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeString = "$date $time"
        val parsedDate = sdf.parse(dateTimeString)
        parsedDate?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}