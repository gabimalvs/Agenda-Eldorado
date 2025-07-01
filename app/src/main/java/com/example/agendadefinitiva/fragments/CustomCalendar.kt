package com.example.agendadefinitiva.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun CustomCalendar(selectedDate: String?, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentYear = calendar.get(Calendar.YEAR)
    val firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} $currentYear",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            daysOfWeek.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            val daysList = (1..daysInMonth).toList()
            itemsIndexed(daysList) { index, day ->
                val dayOfWeek = (firstDayOfWeek + index) % 7
                if (dayOfWeek == 0) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = day.toString(),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(1f)
                                .clickable {
                                    onDateSelected("$day/$currentMonth/$currentYear")
                                },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCustomCalendar() {
    val selectedDate = "01/01/2023"
    val onDateSelected: (String) -> Unit = { }
    CustomCalendar(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected
    )
}



