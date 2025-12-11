package com.example.eventappmanagement.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.request.EventRequest
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import com.example.eventappmanagement.data.result.ApiResult
import com.example.eventappmanagement.ui.component.DropdownField

@Composable
fun UpdateEventScreen(
    eventId: Int,
    viewModel: EventViewModel = viewModel(),
    onBack: () -> Unit,
    onUpdateComplete: () -> Unit
) {
    val state by viewModel.events.collectAsState()

    // LOCAL STATES SEPERTI ADD EVENT
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }

    var location by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val statusList = listOf("upcoming", "ongoing", "completed", "cancelled")
    var selectedStatus by remember { mutableStateOf("upcoming") }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }


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

                // PREFILL — CUMA SEKALI SAAT DATA MASUK
                LaunchedEffect(event.id) {
                    title = event.title
                    description = event.description ?: ""

                    // Split date (YYYY-MM-DD)
                    val partsDate = event.date.split("-")
                    if (partsDate.size == 3) {
                        year = partsDate[0]
                        month = partsDate[1]
                        day = partsDate[2]
                    }

                    // Split time (HH:MM:SS)
                    val partsTime = event.time.split(":")
                    if (partsTime.size == 3) {
                        hour = partsTime[0]
                        minute = partsTime[1]
                        second = partsTime[2]
                    }

                    location = event.location
                    capacity = event.capacity?.toString() ?: ""
                    selectedStatus = event.status
                }

                Column(Modifier.padding(16.dp)) {

                    // Title
                    Text("Update Event", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // ----------------------------
                    // DATE INPUT
                    // ----------------------------
                    Text("Date", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(4.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                        OutlinedTextField(
                            value = day,
                            onValueChange = { if (it.length <= 2) day = it.filter(Char::isDigit) },
                            label = { Text("DD") },
                            modifier = Modifier.weight(1f).padding(end = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = month,
                            onValueChange = { if (it.length <= 2) month = it.filter(Char::isDigit) },
                            label = { Text("MM") },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = year,
                            onValueChange = { if (it.length <= 4) year = it.filter(Char::isDigit) },
                            label = { Text("YYYY") },
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // ----------------------------
                    // TIME INPUT
                    // ----------------------------
                    Text("Time", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(4.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                        OutlinedTextField(
                            value = hour,
                            onValueChange = { if (it.length <= 2) hour = it.filter(Char::isDigit) },
                            label = { Text("HH") },
                            modifier = Modifier.weight(1f).padding(end = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = minute,
                            onValueChange = { if (it.length <= 2) minute = it.filter(Char::isDigit) },
                            label = { Text("MM") },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = second,
                            onValueChange = { if (it.length <= 2) second = it.filter(Char::isDigit) },
                            label = { Text("SS") },
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it.filter(Char::isDigit) },
                        label = { Text("Capacity") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(Modifier.height(16.dp))

                    DropdownField(
                        label = "Status",
                        selected = selectedStatus,
                        options = statusList,
                        onChange = { selectedStatus = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    // ERROR TEXT
                    if (error.isNotEmpty()) {
                        Text(error, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                    }

                    // Final date & time
                    val fullDate = "${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
                    val fullTime = "${hour.padStart(2, '0')}:${minute.padStart(2, '0')}:${second.padStart(2, '0')}"

                    Button(
                        onClick = {
                            if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
                                error = "Tanggal belum lengkap!"
                                return@Button
                            }

                            if (hour.isEmpty() || minute.isEmpty() || second.isEmpty()) {
                                error = "Waktu belum lengkap!"
                                return@Button
                            }

                            loading = true
                            error = ""

                            viewModel.updateEvent(
                                id = eventId,
                                title = title,
                                date = fullDate,
                                time = fullTime,
                                location = location,
                                description = description,
                                capacity = capacity.toIntOrNull() ?: 0,
                                status = selectedStatus
                            ) { success, msg ->
                                loading = false
                                if (success) onUpdateComplete()
                                else error = msg
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading
                    ) {
                        Text(if (loading) "Updating..." else "Update Event")
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            } else {
                Text("Event tidak ditemukan.")
            }
        }
    }
}