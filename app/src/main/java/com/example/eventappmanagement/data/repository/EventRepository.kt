package com.example.eventappmanagement.data.repository

import android.util.Log
import com.example.eventappmanagement.data.di.AppModuleInjection
import com.example.eventappmanagement.data.remote.api.ApiService
import com.example.eventappmanagement.data.remote.request.EventRequest
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import com.example.eventappmanagement.data.remote.response.StatsResponse
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

    suspend fun getEventById(id: Int): ApiResult<SingleEventResponse> {
        return try {
            val response = api.getEventById(id)

            if (response.status == 200) {
                ApiResult.Success(response)
            } else {
                ApiResult.Success(SingleEventResponse(status = response.status, message = response.message, data = null))
            }
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                ApiResult.Success(SingleEventResponse(status = 404, message = "Event dengan ID $id tidak ditemukan", data = null))
            } else {
                ApiResult.Error("Terjadi kesalahan sistem (HTTP ${e.code()})")
            }
        } catch (e: Exception) {
            ApiResult.Error("Terjadi kesalahan sistem: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    suspend fun getEventsByDateRange(dateFrom: String, dateTo: String): ApiResult<MultiEventResponse> {
        return try {
            val response = api.getEventsByDateRange(dateFrom, dateTo)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateEvent(id: Int, request: EventRequest) {
        api.updateEvent(id, request)
    }

    suspend fun deleteEvent(id: Int): ApiResult<SingleEventResponse> {
        return try {
            val response = api.deleteEvent(id)
            ApiResult.Success(response)
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("EventRepository", "HTTP ${e.code()} Error: $errorBody", e)
            ApiResult.Error(e.message ?: "Delete failed")
        } catch (e: Exception) {
            Log.e("EventRepository", "Delete failed", e)
            ApiResult.Error(e.message ?: "Delete failed")
        }
    }

    suspend fun createEvent(request: EventRequest): ApiResult<SingleEventResponse> {
        return try {
            val response = api.createEvent(request)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Create event failed")
        }
    }

    suspend fun getStatistics(): ApiResult<StatsResponse> {
        return try {
            val response = api.getStatistics()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}