package com.example.eventappmanagement.data.remote.api

import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import retrofit2.http.GET

interface ApiService {

    @GET("api.php")
    suspend fun getEvents(): MultiEventResponse

}