package com.example.agendadefinitiva.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendadefinitiva.screens.AddEventScreen
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
    }
}


