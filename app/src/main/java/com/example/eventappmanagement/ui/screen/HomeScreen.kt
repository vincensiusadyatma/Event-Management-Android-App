package com.example.eventappmanagement.ui.screen
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventappmanagement.EventViewModel
import com.example.eventappmanagement.ui.component.StatsCard
import com.example.eventappmanagement.ui.navigation.NavRoutes


@Composable
fun HomeScreen(nav: NavController, vm: EventViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        vm.loadStatistics()
    }

    val stats by vm.stats.collectAsState()
    val loading by vm.statsLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {


        // Header
        Text(
            text = "Event Manager",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Kelola, cari, dan buat event dengan mudah.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )


        // Card Statistik
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Statistik Event",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                when {
                    loading -> {
                        Text(
                            text = "Memuat statistik...",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    stats != null -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatsCard("Total", stats!!.total.toString())
                            StatsCard("Upcoming", stats!!.upcoming)
                            StatsCard("Ongoing", stats!!.ongoing)
                            StatsCard("Selesai", stats!!.completed)
                            StatsCard("Cancelled", stats!!.cancelled)
                        }
                    }
                    else -> {
                        Text(
                            text = "Gagal memuat statistik",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
        // Buttons Event
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ElevatedButton(
                    onClick = { nav.navigate(NavRoutes.ALL_EVENTS) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tampilkan Semua Event")
                }

                OutlinedButton(
                    onClick = { nav.navigate(NavRoutes.CREATE_EVENT) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tambah Event")
                }

                ElevatedButton(
                    onClick = { nav.navigate("event_by_id/0") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cari Event Berdasarkan ID")
                }

                ElevatedButton(
                    onClick = { nav.navigate(NavRoutes.EVENT_BY_DATE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cari Event Berdasarkan Date Range")
                }
            }
        }

        // ====================
        // Footer
        // ====================
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Vincensius Adyatma",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}