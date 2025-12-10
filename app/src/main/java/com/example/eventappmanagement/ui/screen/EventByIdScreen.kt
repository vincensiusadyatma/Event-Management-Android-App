package com.example.eventappmanagement.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import com.example.eventappmanagement.data.result.ApiResult
import com.example.eventappmanagement.ui.component.EventCard

@Composable
fun EventByIdScreen(
    id: Int,
    viewModel: EventViewModel = viewModel(),
    onEventClick: (Int) -> Unit
) {
    var inputId by remember { mutableStateOf(id.toString()) }
    var searchTriggered by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val state by viewModel.events.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = inputId,
            onValueChange = { inputId = it },
            label = { Text("Cari Event Berdasarkan ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val idInt = inputId.toIntOrNull()
                searchTriggered = true
                message = null

                when {
                    idInt == null -> {
                        message = "Masukkan angka yang valid"
                    }
                    idInt == 0 -> {
                        message = "Event dengan ID 0 tidak ditemukan"
                    }
                    else -> {
                        viewModel.searchEventById(idInt)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cari")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tampilkan pesan langsung jika ada
        message?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        // Tampilkan hasil API jika ID valid
        if (searchTriggered && message == null) {
            when (state) {
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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Terjadi Kesalahan",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = (state as ApiResult.Error).message
                                    ?: "Terjadi kesalahan sistem, silakan coba lagi",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                is ApiResult.Success -> {
                    val event = (state as ApiResult.Success<SingleEventResponse>).data.data
                    if (event != null) {
                        EventCard(
                            event = event,
                            onClick = { onEventClick(event.id ?: 0) }
                        )
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "ℹ️ Event dengan ID $inputId tidak ditemukan",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Pastikan ID yang Anda masukkan benar.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
