
package com.example.tuan2bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF304775)
private val Background = Color(0xFFF5F5FA)

// Hàm xếp loại học lực sử dụng if / else
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

// Hàm chọn màu chữ bằng if / else
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
        Color(0xFF858B98)
    }
}

// Hàm chọn màu nền nhãn
fun mauNen(loai: String): Color {
    return if (loai == "Giỏi") {
        Color(0xFFE6F2E9)
    } else if (loai == "Khá") {
        Color(0xFFE8F0FA)
    } else if (loai == "Trung bình") {
        Color(0xFFFFF3DE)
    } else if (loai == "Yếu") {
        Color(0xFFF9E9E7)
    } else {
        Color(0xFFECEEF2)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ManHinhXepLoai()
            }
        }
    }
}

@Composable
fun ManHinhXepLoai() {

    // Điểm trung bình ban đầu
    val diemTrungBinh = 8.5

    // Danh sách điểm kiểm tra thêm
    val danhSachDiem = listOf(9.0, 7.0, 5.5, 3.0, 11.0)

    val ketQua = xepLoai(diemTrungBinh)

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
                    text = "Xếp loại học lực",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Thẻ hiển thị điểm chính
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Điểm trung bình",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = diemTrungBinh.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF202633)
                    )

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                mauNen(ketQua),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ketQua,
                            color = mauXepLoai(ketQua),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(2.dp))

            Text(
                text = "KIỂM TRA THÊM",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            // Danh sách kết quả kiểm tra
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    for ((index, diem) in danhSachDiem.withIndex()) {

                        DongKetQua(diem)

                        if (index < danhSachDiem.lastIndex) {
                            HorizontalDivider(
                                color = Color(0xFFEAEAEA)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Một dòng trong danh sách kiểm tra thêm
@Composable
fun DongKetQua(diem: Double) {

    val loai = xepLoai(diem)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 47.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Điểm ${hienThiDiem(diem)}",
            fontSize = 14.sp,
            color = Color(0xFF202633)
        )

        Text(
            text = loai,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = mauXepLoai(loai),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    mauNen(loai),
                    RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Hiển thị 9 thay vì 9.0, nhưng giữ 5.5
fun hienThiDiem(diem: Double): String {
    return if (diem % 1.0 == 0.0) {
        diem.toInt().toString()
    } else {
        diem.toString()
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruoc() {
    MaterialTheme {
        ManHinhXepLoai()
    }
}
