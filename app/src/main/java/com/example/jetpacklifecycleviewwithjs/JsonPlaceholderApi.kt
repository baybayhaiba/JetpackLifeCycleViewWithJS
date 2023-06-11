package com.example.jetpacklifecycleviewwithjs

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): User?
}