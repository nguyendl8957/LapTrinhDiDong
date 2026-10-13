
package com.example.tuan2bai1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Class Student với email và phone có thể null
data class Student(
    val name: String,
    val email: String?,
    val phone: String?
)

// Khởi tạo danh sách 5 sinh viên
val students = listOf(
    Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
    Student("Trần Thị Bình", null, "0912 888 999"),
    Student("Lê Minh Chi", "chi@sv.edu.vn", null),
    Student("Phạm Quốc Dũng", null, null),
    Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
)

private val Navy = Color(0xFF304775)
private val Background = Color(0xFFF5F5FA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudentScreen()
            }
        }
    }
}

@Composable
fun StudentScreen() {

    // Dùng for để đếm sinh viên thiếu email
    var missingEmail = 0
    for (student in students) {
        if (student.email == null) {
            missingEmail++
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy)
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Danh sách sinh viên",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Dùng for để hiển thị từng sinh viên
            for (student in students) {
                StudentCard(student)
            }

            // Thống kê cuối danh sách
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE5EBF3),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng: ${students.size} sinh viên",
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "Thiếu email: $missingEmail",
                    color = Color(0xFFC34B44),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun StudentCard(student: Student) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF20242D)
            )

            Row {
                Text("Email: ", fontSize = 13.sp)

                Text(
                    text = student.email ?: "Chưa cập nhật",
                    fontSize = 13.sp,
                    color = if (student.email == null)
                        Color(0xFFC34B44)
                    else Color.Gray,
                    fontStyle = if (student.email == null)
                        FontStyle.Italic
                    else FontStyle.Normal
                )
            }

            Row {
                Text("SĐT: ", fontSize = 13.sp)

                Text(
                    text = student.phone ?: "Chưa cập nhật",
                    fontSize = 13.sp,
                    color = if (student.phone == null)
                        Color(0xFFC34B44)
                    else Color.Gray,
                    fontStyle = if (student.phone == null)
                        FontStyle.Italic
                    else FontStyle.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentPreview() {
    MaterialTheme {
        StudentScreen()
    }
}
