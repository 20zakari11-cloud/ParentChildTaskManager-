package com.parentchild.taskmanager.data.model

import androidx.annotation.DrawableRes
import com.parentchild.taskmanager.R

data class Character(
    val id: Int,
    val nameAr: String,
    val nameEn: String,
    val unlockLevel: Int,
    @DrawableRes val drawableRes: Int,
    val emoji: String
)

object Characters {

    val fallback = R.mipmap.ic_launcher

    val all = listOf(
        Character(1, "المبتدئ", "Beginner", 1, fallback, "🐣"),
        Character(2, "المتعلم", "Learner", 2, fallback, "🐥"),
        Character(3, "النشيط", "Active", 3, fallback, "🦊"),
        Character(4, "المجتهد", "Diligent", 5, fallback, "🦁"),
        Character(5, "البطل", "Hero", 7, fallback, "🦅"),
        Character(6, "الأسطورة", "Legend", 10, fallback, "🔥")
    )

    fun getUnlockedFor(level: Int) = all.filter { it.unlockLevel <= level }

    fun getActiveFor(level: Int): Character =
        getUnlockedFor(level).lastOrNull() ?: all.first()
}
