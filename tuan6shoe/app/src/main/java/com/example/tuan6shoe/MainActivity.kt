
package com.example.tuan6shoe

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// Thông tin sản phẩm
data class Product(
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProductScreen()
            }
        }
    }
}

@Composable
fun ProductScreen() {
    var product by remember { mutableStateOf<Product?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    // GỌI API KHI MỞ MÀN HÌNH
    LaunchedEffect(Unit) {
        try {
            product = getProductFromApi()
        } catch (e: Exception) {
            error = e.message ?: "Lỗi kết nối"
            Log.e("SHOE_API", "Lỗi API", e)
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Product detail",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }

        when {
            loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error.isNotEmpty() -> {
                Text("Lỗi: $error", color = Color.Red)
            }

            product != null -> {
                val p = product!!

                Column(
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                ) {
                    Spacer(Modifier.height(20.dp))

                    // HÌNH ẢNH TỪ API
                    ProductImage(p.imageUrl)

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = p.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Giá: ${p.price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    Spacer(Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFF2F2F2),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(15.dp)
                    ) {
                        Text(
                            text = p.description,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 21.sp
                        )
                    }
                }
            }
        }
    }
}

// ==================================
// GỌI API HTTP GET CỦA THẦY
// ==================================
suspend fun getProductFromApi(): Product =
    withContext(Dispatchers.IO) {

        // ĐÂY LÀ LINK API CỦA THẦY
        val apiUrl =
            "https://mock.apidog.com/m1/890655-872447-default/v2/product"

        val connection =
            URL(apiUrl).openConnection() as HttpURLConnection

        try {
            // GỬI YÊU CẦU GET
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.setRequestProperty(
                "Accept", "application/json"
            )

            if (connection.responseCode !in 200..299) {
                throw Exception("HTTP ${connection.responseCode}")
            }

            // NHẬN DỮ LIỆU JSON
            val jsonString = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            Log.d("SHOE_API", jsonString)

            // ĐỌC TỪNG TRƯỜNG JSON
            val json = JSONObject(jsonString)

            Product(
                name = json.getString("name"),
                price = json.getDouble("price"),
                description = json.getString("des"),
                imageUrl = json.getString("imgURL")
            )
        } finally {
            connection.disconnect()
        }
    }

// ==================================
// TẢI HÌNH ẢNH TỪ API
// ==================================
@Composable
fun ProductImage(imageUrl: String) {

    val bitmap by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = imageUrl
    ) {
        value = withContext(Dispatchers.IO) {
            try {
                val connection =
                    URL(imageUrl).openConnection()
                            as HttpURLConnection

                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                try {
                    connection.inputStream.use {
                        BitmapFactory.decodeStream(it)
                            ?.asImageBitmap()
                    }
                } finally {
                    connection.disconnect()
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    val modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .clip(RoundedCornerShape(15.dp))

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = "Ảnh sản phẩm API",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        // Ảnh dự phòng
        Image(
            painter = painterResource(R.drawable.shoe),
            contentDescription = "Ảnh giày",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}