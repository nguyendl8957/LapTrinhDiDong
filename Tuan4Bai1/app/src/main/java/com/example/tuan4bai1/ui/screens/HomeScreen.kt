package com.example.tuan4bai1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan4bai1.data.TaskRepository
import com.example.tuan4bai1.model.Task
import com.example.tuan4bai1.model.TaskStatus

private val Blue = Color(0xFF3B5BE3)

@Composable
fun HomeScreen() {
    var tasks by remember { mutableStateOf(TaskRepository().initialTasks()) }
    Column(
        Modifier.fillMaxSize().background(Color(0xFFF7F8FC))
            .statusBarsPadding().navigationBarsPadding().padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("UTH Todo", fontSize = 24.sp, color = Blue, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Hôm nay bạn muốn hoàn thành gì?", color = Color.Gray)
        Spacer(Modifier.height(24.dp))
        Text("Công việc của tôi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        tasks.forEach { task ->
            TaskCard(task) {
                tasks = tasks.map { current ->
                    if (current.id == task.id) current.copy(
                        status = if (current.status == TaskStatus.COMPLETED)
                            TaskStatus.TODO else TaskStatus.COMPLETED
                    ) else current
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text("Bản thử nghiệm: dữ liệu công việc đang lưu trong bộ nhớ.", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun TaskCard(task: Task, onToggle: () -> Unit) {
    Surface(color = Color.White, shape = RoundedCornerShape(12.dp), shadowElevation = 1.dp) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.status == TaskStatus.COMPLETED, onCheckedChange = { onToggle() })
            Spacer(Modifier.width(7.dp))
            Column {
                Text(task.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(task.category.displayName, fontSize = 12.sp, color = Blue)
            }
        }
    }
}
