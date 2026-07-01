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
    val all = listOf(
        Character(1, "المبتدئ", "Beginner",   unlockLevel = 1,  R.drawable.ic_char_1, "🐣"),
        Character(2, "المتعلم",  "Learner",    unlockLevel = 2,  R.drawable.ic_char_2, "🐥"),
        Character(3, "النشيط",   "Active",     unlockLevel = 3,  R.drawable.ic_char_3, "🦊"),
        Character(4, "المجتهد",  "Diligent",   unlockLevel = 5,  R.drawable.ic_char_4, "🦁"),
        Character(5, "البطل",    "Hero",       unlockLevel = 7,  R.drawable.ic_char_5, "🦅"),
        Character(6, "الأسطورة", "Legend",     unlockLevel = 10, R.drawable.ic_char_6, "🔥")
    )

    fun getUnlockedFor(level: Int) = all.filter { it.unlockLevel <= level }
    fun getActiveFor(level: Int): Character = getUnlockedFor(level).last()
}
