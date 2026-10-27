package com.example.tuan4bai1.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan4bai1.model.IllustrationType
import com.example.tuan4bai1.model.OnboardingPage

private val Blue = Color(0xFF3B5BE3)
private val Muted = Color(0xFF737783)
private val Pale = Color(0xFFEDF0FF)
private val Ink = Color(0xFF151923)

@Composable
fun TodoLogo(modifier: Modifier = Modifier, large: Boolean = false) {
    Box(
        modifier = modifier
            .size(if (large) 82.dp else 26.dp)
            .background(Color.White, RoundedCornerShape(if (large) 20.dp else 7.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize().padding(if (large) 15.dp else 5.dp)) {
            val w = size.width; val h = size.height
            drawCircle(Blue, radius = h * .12f, center = Offset(w * .20f, h * .28f))
            drawLine(Color.White, Offset(w * .16f, h * .28f), Offset(w * .19f, h * .31f), 2.dp.toPx())
            drawLine(Color.White, Offset(w * .19f, h * .31f), Offset(w * .25f, h * .23f), 2.dp.toPx())
            drawRoundRect(Blue, Offset(w * .38f, h * .21f), Size(w * .50f, h * .12f), androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()))
            drawCircle(Color(0xFF9CAAE3), radius = h * .10f, center = Offset(w * .20f, h * .68f), style = Stroke(2.dp.toPx()))
            drawRoundRect(Color(0xFFCAD1E2), Offset(w * .38f, h * .63f), Size(w * .39f, h * .10f), androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()))
        }
    }
}

@Composable
fun SplashScreen(onStart: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Blue)) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TodoLogo(large = true)
            Spacer(Modifier.height(20.dp))
            Text("UTH Todo", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Việc học, việc nhà – gọn gàng mỗi ngày", color = Color.White.copy(alpha = .86f), fontSize = 13.sp)
        }
        Column(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Khám phá", color = Blue, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(12.dp))
            Text("Phiên bản 2.1.0", color = Color.White.copy(alpha = .74f), fontSize = 11.sp)
        }
    }
}

@Composable
fun OnboardingScreen(
    page: OnboardingPage,
    pageIndex: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
            .statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.background(Blue, RoundedCornerShape(8.dp)).padding(2.dp)) {
                TodoLogo()
            }
            Spacer(Modifier.width(8.dp))
            Text("UTH Todo", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onSkip) { Text("Bỏ qua", color = Muted, fontSize = 13.sp) }
        }

        Spacer(Modifier.weight(1.1f))
        Illustration(page.illustration)
        Spacer(Modifier.weight(.9f))

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(page.title, fontSize = 24.sp, lineHeight = 29.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text(page.description, fontSize = 14.sp, lineHeight = 21.sp, color = Muted)
        }
        Spacer(Modifier.height(25.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalPages) { index ->
                Box(
                    Modifier.width(if (index == pageIndex) 21.dp else 6.dp).height(5.dp)
                        .background(if (index == pageIndex) Blue else Color(0xFFD2D5DD), RoundedCornerShape(5.dp))
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(10.dp)
        ) { Text(page.buttonText, color = Color.White, fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Illustration(type: IllustrationType) {
    Box(
        Modifier.size(width = 220.dp, height = 195.dp)
            .background(Pale, RoundedCornerShape(28.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            IllustrationType.CHECKLIST -> {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf(true, false, false).forEach { done ->
                        Surface(shadowElevation = 3.dp, color = Color.White, shape = RoundedCornerShape(9.dp)) {
                            Row(Modifier.fillMaxWidth().height(38.dp).padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(17.dp)
                                        .border(1.dp, if (done) Blue else Color.LightGray, RoundedCornerShape(6.dp))
                                        .background(if (done) Blue else Color.White, RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) { if (done) Text("✓", fontSize = 11.sp, color = Color.White) }
                                Spacer(Modifier.width(10.dp))
                                Box(Modifier.width(if (done) 105.dp else 90.dp).height(5.dp).background(Color(0xFFE3E5EA), RoundedCornerShape(6.dp)))
                            }
                        }
                    }
                }
            }
            IllustrationType.CATEGORY -> {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CategoryPreview("Học tập", Blue)
                    CategoryPreview("Cuộc sống", Color(0xFFD5A24E))
                }
            }
            IllustrationType.REMINDER -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(shape = RoundedCornerShape(60.dp), shadowElevation = 5.dp, color = Color.White) {
                        Box(Modifier.size(72.dp), contentAlignment = Alignment.Center) {
                            Text("♧", fontSize = 42.sp, color = Blue)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Surface(shape = RoundedCornerShape(9.dp), shadowElevation = 3.dp, color = Color.White) {
                        Text("Ôn bài Toán lúc 19:00", Modifier.padding(10.dp), color = Ink, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryPreview(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(11.dp), color = Color.White, shadowElevation = 2.dp) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Box(Modifier.background(color.copy(alpha = .14f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.width(128.dp).height(5.dp).background(Color(0xFFE3E5EA), RoundedCornerShape(5.dp)))
        }
    }
}
