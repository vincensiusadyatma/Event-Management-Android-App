package com.example.eventappmanagement.data.remote.response
import com.example.eventappmanagement.data.model.Event
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val status: Int,
    val message: String? = null,
    val data: List<Event>? = null,
    val timestamp: String? = null
)