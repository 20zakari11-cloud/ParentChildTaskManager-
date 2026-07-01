package com.parentchild.taskmanager.data.model

object Titles {
    private val titles = listOf(
        1  to "المبتدئ الشجاع",
        2  to "المتعلم المثابر",
        3  to "النشيط الصغير",
        4  to "الطالب المجتهد",
        5  to "المنجز الماهر",
        6  to "المحارب الذكي",
        7  to "النجم الصاعد",
        8  to "البطل الحقيقي",
        9  to "المتميز دائماً",
        10 to "الأسطورة الخالدة"
    )

    fun forLevel(level: Int): String {
        val clamped = level.coerceIn(1, titles.size)
        return titles[clamped - 1].second
    }

    fun all() = titles
}
