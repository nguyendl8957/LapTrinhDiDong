package com.example.tuan6bai1.data

data class Task(
    val id: String,
    val title: String,
    val category: Category,
    val dueDate: String,
    val dueTime: String,
    val priority: String,
    val note: String,
    val done: Boolean = false,
    val subtasks: List<Subtask> = emptyList()
)
data class Subtask(val id: String, val title: String, val done: Boolean = false)
enum class Category { STUDY, LIFE }

/** Local preview data only. Replaced by the instructor's Mock REST API in a later step. */
object SampleTasks {
    val today = java.time.LocalDate.now().toString()
    val items = listOf(
        Task("1", "Ôn tập môn Lập trình Android", Category.STUDY, today, "09:00", "high", "Ôn lại Compose và state.", subtasks = listOf(Subtask("s1", "Đọc lại bài giảng"), Subtask("s2", "Làm bài tập tuần 6"))),
        Task("2", "Hoàn thành bài tập tuần 6", Category.STUDY, today, "14:00", "medium", "Nộp bài trước buổi học tiếp theo."),
        Task("3", "Mua đồ dùng cá nhân", Category.LIFE, today, "17:30", "low", ""),
        Task("4", "Dọn dẹp bàn học", Category.LIFE, today, "19:00", "medium", "", done = true),
        Task("5", "Đọc tài liệu Kotlin", Category.STUDY, today, "20:00", "low", "", done = true)
    )
    val toTypedArray get() = items.toTypedArray()
}
