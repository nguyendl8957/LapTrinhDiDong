package com.example.tuan4bai1.model

enum class IllustrationType { CHECKLIST, CATEGORY, REMINDER }

data class OnboardingPage(
    val id: Int,
    val title: String,
    val description: String,
    val illustration: IllustrationType,
    val buttonText: String
)
