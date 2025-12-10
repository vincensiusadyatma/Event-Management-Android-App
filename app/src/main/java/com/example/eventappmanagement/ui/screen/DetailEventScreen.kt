package com.example.eventappmanagement.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import com.example.eventappmanagement.data.result.ApiResult

@Composable
fun DetailEventScreen(
    eventId: Int,
    viewModel: EventViewModel = EventViewModel(),
    onBack: () -> Unit,
    onUpdate: (Int) -> Unit = {},
    onDelete: (Int) -> Unit = {}
) {

    LaunchedEffect(eventId) {
        viewModel.searchEventById(eventId)
    }

    val state by viewModel.events.collectAsState()

    when (state) {
        is ApiResult.Loading -> {
            Text("Loading...")
        }

        is ApiResult.Error -> {
            Text("Error: ${(state as ApiResult.Error).message}")
        }

        is ApiResult.Success -> {
            val response = (state as ApiResult.Success<*>).data as? SingleEventResponse
            val event = response?.data

            if (event != null) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Info Event general
                    Text("Title: ${event.title}", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Description: ${event.description}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Date: ${event.date}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Time: ${event.time}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Location: ${event.location}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onUpdate(event.id!!)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol Hapus
                    Button(
                        onClick = {
                            viewModel.deleteEvent(eventId) {
                                // Callback setelah delete berhasil
                                onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hapus")
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Kembali
                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Kembali")
                    }
                }
            } else {
                Text("Event tidak ditemukan")
            }
        }
    }
}