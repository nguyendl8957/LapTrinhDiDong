package com.example.tuan6bai1.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface TaskApi {

    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("tasks/{id}")
    suspend fun getTaskById(
        @Path("id") id: Int
    ): TaskDto

    @PATCH("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): Response<TaskDto>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Path("id") id: Int
    ): Response<Unit>
}