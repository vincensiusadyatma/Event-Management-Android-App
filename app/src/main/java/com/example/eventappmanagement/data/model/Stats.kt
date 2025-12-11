package com.example.eventappmanagement.data.model
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val total: Int,
    val upcoming: String,
    val ongoing: String,
    val completed: String,
    val cancelled: String
)