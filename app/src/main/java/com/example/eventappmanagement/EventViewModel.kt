package com.example.eventappmanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}