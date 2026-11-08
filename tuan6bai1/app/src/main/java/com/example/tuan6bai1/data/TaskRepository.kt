package com.example.tuan6bai1.data

import com.example.tuan6bai1.data.remote.RetrofitClient
import com.example.tuan6bai1.data.remote.TaskDto

class TaskRepository {

    suspend fun getTasks(): List<TaskDto> {
        return RetrofitClient.api.getTasks()
    }

    suspend fun getTaskById(id: Int): TaskDto {
        return RetrofitClient.api.getTaskById(id)
    }

    suspend fun updateTaskDone(id: Int, done: Boolean): Boolean {
        val response = RetrofitClient.api.updateTask(
            id = id,
            body = mapOf("done" to done)
        )
        return response.isSuccessful
    }

    suspend fun deleteTask(id: Int): Boolean {
        val response = RetrofitClient.api.deleteTask(id)
        return response.isSuccessful
    }
}
