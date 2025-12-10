package com.example.eventappmanagement.data.repository

import com.example.eventappmanagement.data.di.AppModuleInjection
import com.example.eventappmanagement.data.remote.api.ApiService
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.result.ApiResult

class EventRepository {
    private val api: ApiService = AppModuleInjection.eventService

    suspend fun getAllEvents(): ApiResult<MultiEventResponse> {
        return try {
            val response = api.getEvents()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error")
        }
    }
}