package com.example.eventappmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object NavRoutes {
    const val HOME = "home"
    const val ALL_EVENTS = "all_events"
}


@Composable
fun AppNavigation(nav: NavHostController) {
    NavHost(navController = nav, startDestination = NavRoutes.HOME) {

        composable(NavRoutes.ALL_EVENTS) {
             { eventId : Int ->
                nav.navigate("event_detail/$eventId")
            }
        }

    }
}