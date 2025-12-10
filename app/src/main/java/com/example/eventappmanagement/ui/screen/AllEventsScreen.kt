package com.example.eventappmanagement.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.model.Event
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.result.ApiResult
import com.example.eventappmanagement.ui.component.EventCard

@Composable
fun AllEventScreen(viewModel: EventViewModel = EventViewModel(),   onEventClick: (Int) -> Unit) {
    val state by viewModel.events.collectAsState()

    when (state) {
        is ApiResult.Loading -> {
            Text("Loading...")
        }

        is ApiResult.Error -> {
            Text("Error: ${(state as ApiResult.Error).message}")
        }

        is ApiResult.Success -> {
            val response = (state as ApiResult.Success<MultiEventResponse>).data
            val events: List<Event> = response.data ?: emptyList()

            if (events.isEmpty()) {
                Text("Tidak ada event")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(events) { event ->
                        EventCard(event = event)
                    }
                }
            }
        }
    }
}