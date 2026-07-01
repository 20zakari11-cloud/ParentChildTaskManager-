package com.parentchild.taskmanager.data.model

data class GameProgress(
    val stars: Int = 0,
    val level: Int = 1,
    val totalTasksCompleted: Int = 0,
    val activeCharacterId: Int = 1,
    val unlockedCharacterIds: List<Int> = listOf(1)
) {
    val starsToNextLevel: Int get() = STARS_PER_LEVEL - (stars % STARS_PER_LEVEL)
    val levelProgressFraction: Float get() = (stars % STARS_PER_LEVEL) / STARS_PER_LEVEL.toFloat()
    val currentTitle: String get() = Titles.forLevel(level)

    companion object {
        const val STARS_PER_LEVEL = 5

        fun addStar(current: GameProgress): GameProgress {
            val newStars = current.stars + 1
            val newLevel = (newStars / STARS_PER_LEVEL) + 1
            val leveledUp = newLevel > current.level
            val newUnlocked = if (leveledUp) {
                val gained = Characters.all
                    .filter { it.unlockLevel == newLevel && it.id !in current.unlockedCharacterIds }
                    .map { it.id }
                current.unlockedCharacterIds + gained
            } else {
                current.unlockedCharacterIds
            }
            val newActiveChar = if (leveledUp) {
                Characters.getActiveFor(newLevel).id
            } else {
                current.activeCharacterId
            }
            return current.copy(
                stars = newStars,
                level = newLevel,
                totalTasksCompleted = current.totalTasksCompleted + 1,
                activeCharacterId = newActiveChar,
                unlockedCharacterIds = newUnlocked
            )
        }
    }
}
