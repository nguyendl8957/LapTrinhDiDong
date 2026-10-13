
package com.example.tuan2bai3

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// Lớp sinh viên, điểm có thể null
data class Student(
    val name: String,
    val score: Double?
)

// Dữ liệu theo đề bài
val students = listOf(
    Student("Nguyễn Văn An", 9.0),
    Student("Trần Thị Bình", 7.2),
    Student("Lê Minh Chi", null),
    Student("Phạm Quốc Dũng", 4.5),
    Student("Võ Thanh Em", 5.8)
)

private val Navy = Color(0xFF304775)
private val Background = Color(0xFFF5F5FA)

// Hàm xếp loại từ Bài 2
fun xepLoai(diem: Double): String {
    return if (diem < 0 || diem > 10) {
        "Không hợp lệ"
    } else if (diem >= 8.0) {
        "Giỏi"
    } else if (diem >= 6.5) {
        "Khá"
    } else if (diem >= 5.0) {
        "Trung bình"
    } else {
        "Yếu"
    }
}

// Chọn màu chữ theo xếp loại
fun mauXepLoai(loai: String): Color {
    return if (loai == "Giỏi") {
        Color(0xFF38844F)
    } else if (loai == "Khá") {
        Color(0xFF416CB1)
    } else if (loai == "Trung bình") {
        Color(0xFFB98534)
    } else if (loai == "Yếu") {
        Color(0xFFBD514B)
    } else {
        Color.Gray
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ManHinhBangDiem()
            }
        }
    }
}

@Composable
fun ManHinhBangDiem() {

    // Các biến dùng để thống kê
    var tongDiem = 0.0
    var soDaCoDiem = 0
    var soChuaCoDiem = 0
    var soSinhVienGioi = 0
    var diemCaoNhat: Double? = null

    // Dùng for và if để tính thống kê
    for (student in students) {
        val diem = student.score

        if (diem == null) {
            soChuaCoDiem++
        } else {
            tongDiem += diem
            soDaCoDiem++

            if (xepLoai(diem) == "Giỏi") {
                soSinhVienGioi++
            }

            if (diemCaoNhat == null ||
                diem > diemCaoNhat) {
                diemCaoNhat = diem
            }
        }
    }

    // Tính trung bình, tránh chia cho 0
    val diemTrungBinh = if (soDaCoDiem > 0) {
        tongDiem / soDaCoDiem
    } else {
        0.0
    }

    val diemTrungBinhText = String.format(
        Locale.US, "%.2f", diemTrungBinh
    )

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
                    text = "Bảng điểm lớp",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
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
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Khung thống kê màu xanh
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Navy,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Điểm trung bình lớp",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                Text(
                    text = diemTrungBinhText,
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(14.dp))

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.2f)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ThongKeNho(
                        "Giỏi",
                        soSinhVienGioi.toString()
                    )

                    ThongKeNho(
                        "Chưa có điểm",
                        soChuaCoDiem.toString()
                    )

                    ThongKeNho(
                        "Cao nhất",
                        diemCaoNhat?.toString() ?: "--"
                    )
                }
            }

            Text(
                text = "DANH SÁCH · ${students.size} SINH VIÊN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            // Hiển thị danh sách bằng vòng lặp for
            for (student in students) {
                TheSinhVien(student)
            }
        }
    }
}

// Một cột thống kê nhỏ
@Composable
fun ThongKeNho(tieuDe: String, giaTri: String) {
    Column {
        Text(
            text = tieuDe,
            color = Color.LightGray,
            fontSize = 11.sp
        )

        Text(
            text = giaTri,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Thẻ hiển thị sinh viên
@Composable
fun TheSinhVien(student: Student) {

    val loai = if (student.score == null) {
        "Chưa có điểm"
    } else {
        xepLoai(student.score)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = student.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202633)
                )

                Text(
                    text = loai,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = mauXepLoai(loai)
                )
            }

            Text(
                text = student.score?.toString() ?: "—",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = if (student.score == null)
                    Color.Gray
                else Color(0xFF202633)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocBangDiem() {
    MaterialTheme {
        ManHinhBangDiem()
    }
}
