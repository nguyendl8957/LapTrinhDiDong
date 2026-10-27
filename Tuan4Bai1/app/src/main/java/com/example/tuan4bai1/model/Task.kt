package com.example.tuan4bai1.model

enum class TaskPriority { LOW, MEDIUM, HIGH }
enum class TaskStatus { TODO, IN_PROGRESS, COMPLETED }

data class Task(
    val id: Long,
    val title: String,
    val description: String = "",
    val category: TaskCategory,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.TODO,
    val dueAt: String? = null
)
