package com.example.eventappmanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventappmanagement.data.model.Stats
import com.example.eventappmanagement.data.remote.request.EventRequest
import com.example.eventappmanagement.data.repository.EventRepository
import com.example.eventappmanagement.data.result.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repo = EventRepository()
    private val _events = MutableStateFlow<ApiResult<Any>>(ApiResult.Loading)
    val events: StateFlow<ApiResult<Any>> = _events

    fun loadEvents() {
        viewModelScope.launch {
            _events.value = ApiResult.Loading
            _events.value = repo.getAllEvents()
        }
    }

    fun searchEventById(id: Int) {
        viewModelScope.launch {
            _events.value = ApiResult.Loading
            _events.value = repo.getEventById(id)
        }
    }

    fun getEventsByDateRange(dateFrom: String, dateTo: String) {
        viewModelScope.launch {
            _events.value = ApiResult.Loading
            try {
                val result = repo.getEventsByDateRange(dateFrom, dateTo)
                _events.value = result
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("EventViewModel", "HTTP ${e.code()} Error: $errorBody", e)
                _events.value = ApiResult.Error("Failed to load events: ${e.message()}")
            } catch (e: Exception) {
                Log.e("EventViewModel", "Date range error", e)
                _events.value = ApiResult.Error(e.message ?: "Failed to load events")
            }
        }
    }

    fun updateEvent(
        id: Int,
        title: String,
        date: String,
        time: String,
        location: String,
        description: String?,
        capacity: Int,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = EventRequest(
                    title = title,
                    date = date,
                    time = time,
                    location = location,
                    description = description,
                    capacity = capacity,
                    status = status
                )

                repo.updateEvent(id, request)

                // Update ulang daftar event
                _events.value = ApiResult.Loading
                loadEvents()

                callback(true, "Event updated successfully")

            } catch (e: Exception) {
                callback(false, e.message ?: "Failed to update event")
            }
        }
    }


    fun deleteEvent(id: Int, onDeleteComplete: () -> Unit) {
        viewModelScope.launch {
            _events.value = ApiResult.Loading
            try {
                val result = repo.deleteEvent(id)
                if (result is ApiResult.Success) {
                    Log.d("EventViewModel", "Delete successful: $id")
                    loadEvents()
                    onDeleteComplete()
                } else if (result is ApiResult.Error) {
                    Log.e("EventViewModel", "Delete failed: ${result.message}")
                    _events.value = ApiResult.Error(result.message ?: "Delete failed")
                }
            } catch (e: Exception) {
                Log.e("EventViewModel", "Delete failed", e)
                _events.value = ApiResult.Error(e.message ?: "Delete failed")
            }
        }
    }

    fun createEvent(
        title: String,
        date: String,
        time: String,
        location: String,
        description: String,
        capacity: Int,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = EventRequest(
                    title = title,
                    date = date,
                    time = time,
                    location = location,
                    description = description,
                    capacity = capacity,
                    status = status
                )

                val result = repo.createEvent(request)

                when (result) {
                    is ApiResult.Success -> {
                        val response = result.data
                        if (response.status == 1) {
                            callback(true, response.message ?: "Success")
                        } else {
                            callback(false, response.message ?: "Failed")
                        }
                    }
                    is ApiResult.Error -> {
                        callback(false, result.message ?: "Unknown error")
                    }
                    else -> Unit
                }

            } catch (e: Exception) {
                callback(false, e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private val _stats = MutableStateFlow<Stats?>(null)
    val stats: StateFlow<Stats?> get() = _stats

    private val _statsLoading = MutableStateFlow(false)
    val statsLoading: StateFlow<Boolean> get() = _statsLoading

    fun loadStatistics() {
        viewModelScope.launch {
            _statsLoading.value = true
            when (val result = repo.getStatistics()) {
                is ApiResult.Success -> {
                    _stats.value = result.data.data
                }
                is ApiResult.Error -> {
                    _stats.value = null
                }
                is ApiResult.Loading -> {
                    _stats.value = null
                }
            }

            _statsLoading.value = false
        }
    }


}