package com.example.eventappmanagement.data.di
import com.example.eventappmanagement.data.remote.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

object AppModuleInjection {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://104.248.153.158/event-api/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val eventService: ApiService = retrofit.create()
}