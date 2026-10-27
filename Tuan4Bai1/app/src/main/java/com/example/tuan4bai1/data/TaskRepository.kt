package com.example.tuan4bai1.data

import com.example.tuan4bai1.model.Task
import com.example.tuan4bai1.model.TaskCategory

class TaskRepository {
    fun initialTasks(): List<Task> = listOf(
        Task(1, "Ôn bài Kotlin và OOP", category = TaskCategory.STUDY, dueAt = "19:00"),
        Task(2, "Hoàn thành bài tập di động", category = TaskCategory.STUDY),
        Task(3, "Dọn góc học tập", category = TaskCategory.PERSONAL)
    )
}
