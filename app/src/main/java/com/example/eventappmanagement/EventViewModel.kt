package com.example.eventappmanagement

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
}