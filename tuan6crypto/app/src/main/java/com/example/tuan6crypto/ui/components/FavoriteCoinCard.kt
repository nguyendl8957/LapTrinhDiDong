package com.example.tuan6crypto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan6crypto.data.model.CoinUiModel
import com.example.tuan6crypto.ui.theme.*
import java.util.Locale

@Composable
fun FavoriteCoinCard(
    coin: CoinUiModel,
    modifier: Modifier = Modifier
) {
    val priceText = coin.priceUsd?.let {
        String.format(Locale.US, "$%,.2f", it)
    } ?: "—"

    val changeText = coin.changePercent?.let {
        String.format(Locale.US, "%+.2f%%", it)
    } ?: "—"

    val changeColor = when {
        coin.changePercent == null -> CryptoMuted
        coin.changePercent > 0 -> CryptoGreen
        coin.changePercent < 0 -> CryptoRed
        else -> CryptoMuted
    }

    Column(
        modifier = modifier
            .background(CryptoCard, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinIcon(coin.type, size = 38.dp)

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = coin.type.symbol,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = priceText,
                color = Color.White,
                fontSize = if (coin.type.symbol == "BTC") 13.sp else 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = changeText,
                color = changeColor,
                fontSize = 11.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            PriceSparkline(
                prices = coin.priceHistory,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}