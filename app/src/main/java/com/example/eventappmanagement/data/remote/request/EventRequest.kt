package com.example.eventappmanagement.data.remote.request
import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String? = null,
    val capacity: Int? = null,
    val status: String
)