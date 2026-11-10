package com.example.tuan6crypto.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tuan6crypto.R

@Composable
fun BreakingNewsBanner(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.breaking_news),
        contentDescription = "Breaking News",
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}