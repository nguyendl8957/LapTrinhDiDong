package com.example.tuan4bai1.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuan4bai1.data.OnboardingRepository

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashPreview() { MaterialTheme { SplashScreen(onStart = {}) } }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FirstOnboardingPreview() {
    MaterialTheme {
        OnboardingScreen(
            page = OnboardingRepository().getPages()[0],
            pageIndex = 0,
            totalPages = 3,
            onNext = {}, onSkip = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() { MaterialTheme { HomeScreen() } }
