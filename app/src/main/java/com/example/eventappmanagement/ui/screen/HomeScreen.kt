package com.example.eventappmanagement.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventappmanagement.ui.navigation.NavRoutes

@Composable
fun HomeScreen(nav: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Event App", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { nav.navigate(NavRoutes.ALL_EVENTS) }, modifier = Modifier.fillMaxWidth()) {
            Text("Tampilkan Semua Event")
        }

        Button(
            onClick = { nav.navigate("event_by_id/0") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cari Event Berdasarkan ID")
        }


    }

}