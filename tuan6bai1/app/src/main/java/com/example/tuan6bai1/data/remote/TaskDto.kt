package com.example.tuan6bai1.data.remote

data class SubtaskDto(
    val title: String,
    val done: Boolean
)

data class TaskDto(
    val id: Int,
    val title: String,
    val note: String,
    val category: String,
    val date: String,
    val time: String,
    val priority: String,
    val remind: Boolean,
    val done: Boolean,
    val subtasks: List<SubtaskDto>
)
