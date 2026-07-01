package com.parentchild.taskmanager.data.repository

import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.GameProgress

/**
 * Manages gamification state. Firebase migration path:
 *   - Write progress to Firestore document "users/{childUid}/progress"
 *   - Transactions ensure star increments are atomic
 */
class ProgressRepository(private val prefs: SharedPrefsManager) {

    fun getProgress(): GameProgress = prefs.getGameProgress()

    fun addStar(): Pair<GameProgress, Boolean> {
        val current = prefs.getGameProgress()
        val oldLevel = current.level
        val updated = GameProgress.addStar(current)
        prefs.saveGameProgress(updated)
        val leveledUp = updated.level > oldLevel
        return updated to leveledUp
    }

    fun setActiveCharacter(characterId: Int) {
        val current = prefs.getGameProgress()
        if (characterId in current.unlockedCharacterIds) {
            prefs.saveGameProgress(current.copy(activeCharacterId = characterId))
        }
    }

    fun resetProgress() {
        prefs.saveGameProgress(GameProgress())
    }
}
