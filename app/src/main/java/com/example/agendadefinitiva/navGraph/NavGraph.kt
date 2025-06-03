package com.example.agendadefinitiva.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agendadefinitiva.screens.AddEventScreen
import com.example.agendadefinitiva.screens.EventDetailScreen
import com.example.agendadefinitiva.screens.MainScreen
import com.example.agendadefinitiva.viewmodel.EventViewModel

@Composable
fun NavGraph(eventViewModel: EventViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            MainScreen(navController, eventViewModel)
        }
        composable("addEvent") {
            AddEventScreen(navController, eventViewModel)
        }
        composable(
            "eventDetail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId")
            val events by eventViewModel.events.collectAsState()
            val event = events.find { it.id == eventId }

            if (event != null) {
                EventDetailScreen(navController, event)
            } else {
                androidx.compose.material3.Text("Event not found")
            }
        }
    }
}


