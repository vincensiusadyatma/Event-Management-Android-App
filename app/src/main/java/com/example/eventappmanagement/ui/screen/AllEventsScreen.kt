package com.example.eventappmanagement.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.result.ApiResult
import com.example.eventappmanagement.ui.component.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllEventsScreen(
    viewModel: EventViewModel = EventViewModel(),
    onEventClick: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    val state by viewModel.events.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Semua Event") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        when (state) {

            is ApiResult.Loading -> Text(
                "Loading...",
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            is ApiResult.Error ->
            {
                Log.e("ERROR FETCH:" , "${(state as ApiResult.Error).message}")
                Text(
                    "Error: ${(state as ApiResult.Error).message}",
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }



            is ApiResult.Success -> {
                val response = (state as ApiResult.Success<*>).data as? MultiEventResponse
                val events = response?.data ?: emptyList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    items(events) { event ->
                        event.id?.let { id ->
                            EventCard(
                                event = event,
                                onClick = { onEventClick(id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
