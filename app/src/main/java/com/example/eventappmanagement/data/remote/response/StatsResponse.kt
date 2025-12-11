package com.example.eventappmanagement.data.remote.response

import com.example.eventappmanagement.data.model.Stats
import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    val status: Int,
    val message: String? = null,
    val data: Stats? = null,
    val timestamp: String? = null
)

