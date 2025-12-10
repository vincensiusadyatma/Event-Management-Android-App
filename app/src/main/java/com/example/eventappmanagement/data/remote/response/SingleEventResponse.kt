package com.example.eventappmanagement.data.remote.response

import com.example.eventappmanagement.data.model.Event

data class SingleEventResponse(
    val status: Int,
    val message: String? = null,
    val data: Event? = null,
    val timestamp: String? = null
)