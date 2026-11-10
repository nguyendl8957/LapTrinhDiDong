package com.example.tuan6crypto.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun PriceSparkline(
    prices: List<Double>,
    modifier: Modifier = Modifier
) {
    if (prices.size < 2) return

    val lineColor = Color(0xFF648CFF)

    Canvas(modifier = modifier) {
        val minimum = prices.minOrNull() ?: return@Canvas
        val maximum = prices.maxOrNull() ?: return@Canvas
        val range = (maximum - minimum).takeIf { it > 0.0 }
            ?: 1.0

        val horizontalStep =
            size.width / (prices.size - 1)

        val topPadding = size.height * 0.10f
        val chartHeight = size.height * 0.78f

        val line = Path()
        val area = Path()

        prices.forEachIndexed { index, price ->
            val x = index * horizontalStep
            val normalized = ((price - minimum) / range).toFloat()
            val y = topPadding + (1f - normalized) * chartHeight

            if (index == 0) {
                line.moveTo(x, y)
                area.moveTo(x, size.height)
                area.lineTo(x, y)
            } else {
                line.lineTo(x, y)
                area.lineTo(x, y)
            }
        }

        area.lineTo(size.width, size.height)
        area.close()

        // Vùng xanh mờ dưới đường giá
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                listOf(
                    lineColor.copy(alpha = 0.30f),
                    lineColor.copy(alpha = 0.01f)
                )
            )
        )

        // Đường giá xanh lam
        drawPath(
            path = line,
            color = lineColor,
            style = Stroke(width = 2.2f)
        )
    }
}