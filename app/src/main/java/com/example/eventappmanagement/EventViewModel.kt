package com.example.eventappmanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun updateEvent(id: Int, request: EventRequest) {
        viewModelScope.launch {
            try {
                repo.updateEvent(id, request)
                _events.value = ApiResult.Loading
                loadEvents()
            } catch (e: Exception) {
                Log.e("EventViewModel", "Update failed", e)
                _events.value = ApiResult.Error(e.message ?: "Update failed")
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
}