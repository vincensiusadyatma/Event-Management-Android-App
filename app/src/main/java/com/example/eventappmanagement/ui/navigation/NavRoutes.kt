package com.example.eventappmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventappmanagement.ui.screen.AllEventsScreen
import com.example.eventappmanagement.ui.screen.DetailEventScreen
import com.example.eventappmanagement.ui.screen.EventByDateRangeScreen
import com.example.eventappmanagement.ui.screen.EventByIdScreen
import com.example.eventappmanagement.ui.screen.HomeScreen

object NavRoutes {
    const val HOME = "home"
    const val ALL_EVENTS = "all_events"
    const val EVENT_BY_ID = "event_by_id/{id}"
    const val EVENT_BY_DATE = "event_by_date"
    const val EVENT_DETAIL = "event_detail/{eventId}"
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

        composable(NavRoutes.EVENT_BY_ID) { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            EventByIdScreen(
                id = id,
                onEventClick = { eventId ->
                    nav.navigate("event_detail/$eventId")
                }
            )
        }

        composable(NavRoutes.EVENT_BY_DATE) {
            EventByDateRangeScreen(
                nav = nav,
                onEventClick = { id ->
                    nav.navigate("event_detail/$id")
                }
            )
        }

        composable(NavRoutes.EVENT_DETAIL) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: 0
            DetailEventScreen(
                eventId = eventId,
                onBack = { nav.popBackStack() },
                onUpdate = { id -> nav.navigate("update_event/$id") },
                onDelete = { id ->  }
            )
        }

    }
}