package com.example.eventappmanagement.data.remote.response
import com.example.eventappmanagement.data.model.Event
import kotlinx.serialization.Serializable

@Serializable
data class MultiEventResponse(
    val status: Int,
    val message: String? = null,
    val data: List<Event>? = null,
    val timestamp: String? = null
)