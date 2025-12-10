package com.example.eventappmanagement.data.remote.api

import com.example.eventappmanagement.data.remote.request.EventRequest
import com.example.eventappmanagement.data.remote.response.MultiEventResponse
import com.example.eventappmanagement.data.remote.response.SingleEventResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @GET("api.php")
    suspend fun getEvents(): MultiEventResponse

    @GET("api.php")
    suspend fun getEventById(@Query("id") id: Int): SingleEventResponse

    @GET("api.php")
    suspend fun getEventsByDateRange(
        @Query("date_from") dateFrom: String,
        @Query("date_to") dateTo: String
    ): MultiEventResponse

    @PUT("api.php")
    suspend fun updateEvent(
        @Query("id") id: Int,
        @Body request: EventRequest
    ): SingleEventResponse

    @DELETE("api.php")
    suspend fun deleteEvent(@Query("id") id: Int): SingleEventResponse

    @POST("api.php")
    suspend fun createEvent(
        @Body request: EventRequest
    ): SingleEventResponse


}