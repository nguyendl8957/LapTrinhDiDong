package com.example.tuan4bai1.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.tuan4bai1.data.OnboardingRepository
import com.example.tuan4bai1.ui.screens.HomeScreen
import com.example.tuan4bai1.ui.screens.OnboardingScreen
import com.example.tuan4bai1.ui.screens.SplashScreen

private enum class Screen { SPLASH, ONBOARDING, HOME }

@Composable
fun AppNavigation() {
    val pages = remember { OnboardingRepository().getPages() }
    var screen by rememberSaveable { mutableStateOf(Screen.SPLASH.name) }
    var pageIndex by rememberSaveable { mutableIntStateOf(0) }

    BackHandler(enabled = screen == Screen.ONBOARDING.name || screen == Screen.HOME.name) {
        when (screen) {
            Screen.ONBOARDING.name -> {
                if (pageIndex > 0) pageIndex-- else screen = Screen.SPLASH.name
            }
            Screen.HOME.name -> {
                // Back ve onboarding cuoi de minh hoa navigation.
                pageIndex = pages.lastIndex
                screen = Screen.ONBOARDING.name
            }
        }
    }

    when (screen) {
        Screen.SPLASH.name -> SplashScreen(
            onStart = { pageIndex = 0; screen = Screen.ONBOARDING.name }
        )
        Screen.ONBOARDING.name -> OnboardingScreen(
            page = pages[pageIndex],
            pageIndex = pageIndex,
            totalPages = pages.size,
            onNext = {
                if (pageIndex < pages.lastIndex) pageIndex++
                else screen = Screen.HOME.name
            },
            onSkip = { screen = Screen.HOME.name }
        )
        else -> HomeScreen()
    }
}
