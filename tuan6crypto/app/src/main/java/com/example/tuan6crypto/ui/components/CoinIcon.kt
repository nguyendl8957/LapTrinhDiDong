package com.example.tuan6crypto.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tuan6crypto.data.model.CoinType
import com.example.tuan6crypto.ui.theme.CryptoBlue
import com.example.tuan6crypto.ui.theme.CryptoIconBackground

@Composable
fun CoinIcon(
    type: CoinType,
    size: Dp = 42.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(10.dp))
            .background(CryptoIconBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = type.iconRes),
            contentDescription = type.displayName,
            modifier = Modifier.size(size * 0.60f),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(CryptoBlue)
        )
    }
}