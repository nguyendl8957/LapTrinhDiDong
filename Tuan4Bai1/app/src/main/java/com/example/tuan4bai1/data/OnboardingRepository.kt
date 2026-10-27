package com.example.tuan4bai1.data

import com.example.tuan4bai1.model.IllustrationType
import com.example.tuan4bai1.model.OnboardingPage

class OnboardingRepository {
    fun getPages(): List<OnboardingPage> = listOf(
        OnboardingPage(
            1, "Mọi việc, gọn trong một chỗ",
            "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
            IllustrationType.CHECKLIST, "Tiếp tục"
        ),
        OnboardingPage(
            2, "Chia theo Học tập và Cuộc sống",
            "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
            IllustrationType.CATEGORY, "Tiếp tục"
        ),
        OnboardingPage(
            3, "Nhắc đúng lúc",
            "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
            IllustrationType.REMINDER, "Bắt đầu ngay"
        )
    )
}
