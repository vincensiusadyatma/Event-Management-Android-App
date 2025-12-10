package com.example.eventappmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventappmanagement.ui.screen.AllEventsScreen
import com.example.eventappmanagement.ui.screen.HomeScreen

object NavRoutes {
    const val HOME = "home"
    const val ALL_EVENTS = "all_events"
}


@Composable
fun AppNavigation(nav: NavHostController) {
    NavHost(navController = nav, startDestination = NavRoutes.HOME) {

        composable(NavRoutes.HOME) { HomeScreen(nav) }

        composable(NavRoutes.ALL_EVENTS) {
            AllEventsScreen(
                onEventClick = { eventId ->
                    nav.navigate("event_detail/$eventId")
                }
            )
        }


    }
}