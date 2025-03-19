package com.example.challenge.data.remote.service.log_in

import com.example.challenge.data.remote.model.log_in.LogInDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LogInService {
    @GET("9e7e33ad-7152-45c2-8fbb-8940f9969ace")
    suspend fun logIn(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<LogInDto>
}