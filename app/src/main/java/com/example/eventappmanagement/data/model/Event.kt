package com.example.eventappmanagement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int? = null,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String? = null,
    val capacity: Int? = null,
    val status: String
)