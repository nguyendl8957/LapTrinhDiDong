package com.example.thuchanh3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Activity chính của ứng dụng
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ManHinhMayTinh()
        }
    }
}

// Hàm xây dựng giao diện máy tính
@Composable
fun ManHinhMayTinh() {

    // Ghi nhớ giá trị trong hai ô nhập
    var soThuNhat by remember { mutableStateOf("") }
    var soThuHai by remember { mutableStateOf("") }

    // Ghi nhớ phép toán đang được chọn
    var phepToan by remember { mutableStateOf("") }

    // Ghi nhớ dòng kết quả
    var ketQua by remember { mutableStateOf("") }


    // Hàm thực hiện phép tính
    fun tinhToan(phepTinh: String) {

        // Lưu phép toán vừa được chọn
        phepToan = phepTinh

        // Kiểm tra người dùng đã nhập đủ hai số chưa
        if (soThuNhat.isBlank() || soThuHai.isBlank()) {
            ketQua = "Vui lòng nhập đủ hai số"
            return
        }

        // Chuyển chuỗi thành số Double
        val a = soThuNhat.toDoubleOrNull()
        val b = soThuHai.toDoubleOrNull()

        // Kiểm tra dữ liệu không hợp lệ
        if (a == null || b == null ||
            !a.isFinite() || !b.isFinite()) {
            ketQua = "Số nhập vào không hợp lệ"
            return
        }

        // Ngăn phép chia cho số 0
        if (phepTinh == "/" && b == 0.0) {
            ketQua = "Không thể chia cho 0"
            return
        }

        // Thực hiện phép toán được chọn
        val ketQuaSo = when (phepTinh) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b
            else -> null
        }

        // Cập nhật nội dung hiển thị
        ketQua = when {
            ketQuaSo == null -> "Phép toán không hợp lệ"
            !ketQuaSo.isFinite() -> "Kết quả vượt giới hạn"
            else -> ketQuaSo.toString()
        }
    }


    // Toàn bộ giao diện được xếp theo chiều dọc
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Khoảng trống phía trên
        Spacer(modifier = Modifier.height(90.dp))

        // Tiêu đề ứng dụng
        Text(
            text = "Thực hành 03",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Ô nhập số thứ nhất
        OutlinedTextField(
            value = soThuNhat,
            onValueChange = {
                soThuNhat = it
                ketQua = ""
                phepToan = ""
            },
            label = { Text("Số thứ nhất") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hàng chứa bốn nút phép toán
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {



            NutPhepToan(
                kyHieu = "+",
                mauSac = Color(0xFFC9544C),
                duocChon = phepToan == "+"
            ) {
                tinhToan("+")
            }

            NutPhepToan(
                kyHieu = "-",
                mauSac = Color(0xFFD7AE59),
                duocChon = phepToan == "-"
            ) {
                tinhToan("-")
            }

            NutPhepToan(
                kyHieu = "*",
                mauSac = Color(0xFF6250DC),
                duocChon = phepToan == "*"
            ) {
                tinhToan("*")
            }

            NutPhepToan(
                kyHieu = "/",
                mauSac = Color(0xFF202020),
                duocChon = phepToan == "/"
            ) {
                tinhToan("/")
            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ô nhập số thứ hai
        OutlinedTextField(
            value = soThuHai,
            onValueChange = {
                soThuHai = it
                ketQua = ""
                phepToan = ""
            },
            label = { Text("Số thứ hai") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dòng hiển thị kết quả
        Text(
            text = "Kết quả: $ketQua",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp
        )
    }
}

// Hàm tạo nút phép toán có thể dùng lại
@Composable
fun NutPhepToan(
    kyHieu: String,
    mauSac: Color,
    duocChon: Boolean,
    khiBam: () -> Unit
) {
    Button(
        onClick = khiBam,
        modifier = Modifier
            .size(55.dp)
            .border(
                width = if (duocChon) 3.dp else 0.dp,
                color = if (duocChon) Color.Black
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mauSac
        )
    ) {
        Text(
            text = kyHieu,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

// Xem trước giao diện trong Android Studio
@Preview(showBackground = true)
@Composable
fun XemTruocMayTinh() {
    MaterialTheme {
        ManHinhMayTinh()
    }
}
