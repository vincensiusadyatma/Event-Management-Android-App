package com.example.eventappmanagement.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.request.EventRequest
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import com.example.eventappmanagement.data.result.ApiResult

@Composable
fun UpdateEventScreen(
    eventId: Int,
    viewModel: EventViewModel = viewModel(),
    onBack: () -> Unit,
    onUpdateComplete: () -> Unit
) {
    val state by viewModel.events.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf<Int?>(null) }


    LaunchedEffect(eventId) { viewModel.searchEventById(eventId) }

    when (val s = state) {
        is ApiResult.Loading -> Text("Loading...")
        is ApiResult.Error -> Text(s.message)
        is ApiResult.Success<*> -> {
            val response = s.data
            val event = when (response) {
                is SingleEventResponse -> response.data
                else -> null
            }

            if (event != null) {
                LaunchedEffect(Unit) {
                    title = event.title
                    description = event.description ?: ""
                    date = event.date
                    time = event.time
                    location = event.location
                    status = event.status
                    capacity = event.capacity
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") })
                    OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time (HH:MM:SS)") })
                    OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                    OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Status") })
                    OutlinedTextField(
                        value = capacity?.toString() ?: "",
                        onValueChange = { capacity = it.toIntOrNull() },
                        label = { Text("Capacity") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val request = EventRequest(
                                title = title,
                                date = date,
                                time = time,
                                location = location,
                                status = status,
                                description = description,
                                capacity = capacity
                            )
                            viewModel.updateEvent(eventId, request)
                            onUpdateComplete()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Update") }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Batal")
                    }
                }
            } else {
                Text("Event tidak ditemukan atau response salah")
            }
        }
    }
}