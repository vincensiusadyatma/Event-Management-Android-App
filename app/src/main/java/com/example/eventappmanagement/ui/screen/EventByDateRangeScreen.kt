package com.example.eventappmanagement.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.result.ApiResult
import com.example.eventappmanagement.ui.component.DropdownField
import com.example.eventappmanagement.ui.component.EventCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment


fun buildDays(month: String, year: String): List<String> {
    val m = month.toInt()
    val y = year.toInt()

    val days = when (m) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(y)) 29 else 28
        else -> 31
    }

    return (1..days).map { "%02d".format(it) }
}

fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)



@Composable
fun EventByDateRangeScreen(
    nav: NavHostController,
    viewModel: EventViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onEventClick: (Int) -> Unit
) {
    var fromDay by remember { mutableStateOf("01") }
    var fromMonth by remember { mutableStateOf("01") }
    var fromYear by remember { mutableStateOf("2025") }

    var toDay by remember { mutableStateOf("01") }
    var toMonth by remember { mutableStateOf("01") }
    var toYear by remember { mutableStateOf("2025") }


    var isSearchTriggered by remember { mutableStateOf(false) }

    val eventState by viewModel.events.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Filter Event by Date Range", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        // FROM DATE
        Text("From", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth()) {
            DropdownField("DD", fromDay, buildDays(fromMonth, fromYear)) { fromDay = it }
            Spacer(Modifier.width(8.dp))
            DropdownField("MM", fromMonth, (1..12).map { "%02d".format(it) }) { fromMonth = it }
            Spacer(Modifier.width(8.dp))
            DropdownField("YYYY", fromYear, (2023..2030).map { it.toString() }) { fromYear = it }
        }

        Spacer(Modifier.height(20.dp))

        // TO DATE
        Text("To", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth()) {
            DropdownField("DD", toDay, buildDays(toMonth, toYear)) { toDay = it }
            Spacer(Modifier.width(8.dp))
            DropdownField("MM", toMonth, (1..12).map { "%02d".format(it) }) { toMonth = it }
            Spacer(Modifier.width(8.dp))
            DropdownField("YYYY", toYear, (2023..2030).map { it.toString() }) { toYear = it }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                isSearchTriggered = true  // User menekan Search
                val start = "$fromYear-$fromMonth-$fromDay"
                val end = "$toYear-$toMonth-$toDay"
                viewModel.getEventsByDateRange(start, end)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(20.dp))


        if (isSearchTriggered) {
            when (eventState) {
                is ApiResult.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResult.Error -> {
                    Text(
                        text = (eventState as ApiResult.Error).message ?: "Terjadi kesalahan",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is ApiResult.Success -> {
                    val response = (eventState as ApiResult.Success<*>).data as? MultiEventResponse
                    val events = response?.data ?: emptyList()

                    if (events.isEmpty()) {
                        Text("Tidak ada event pada rentang tanggal ini")
                    } else {
                        LazyColumn {
                            items(events) { event ->
                                EventCard(
                                    event = event,
                                    onClick = { event.id?.let { onEventClick(it) } }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

