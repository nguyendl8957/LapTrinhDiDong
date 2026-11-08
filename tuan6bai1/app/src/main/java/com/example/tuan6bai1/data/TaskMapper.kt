package com.example.tuan6bai1.data

import com.example.tuan6bai1.data.remote.TaskDto

fun TaskDto.toTask(): Task {
    return Task(
        id = id.toString(),
        title = title,
        category = when (category.lowercase()) {
            "study" -> Category.STUDY
            "life" -> Category.LIFE
            else -> Category.STUDY
        },
        dueDate = date,
        dueTime = time,
        priority = priority,
        note = note,
        done = done,
        subtasks = subtasks.mapIndexed { index, item ->
            Subtask(
                id = "${id}_$index",
                title = item.title,
                done = item.done
            )
        }
    )
}
