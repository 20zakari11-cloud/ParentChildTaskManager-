package com.parentchild.taskmanager.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parentchild.taskmanager.data.model.BedtimeState
import com.parentchild.taskmanager.data.model.GameProgress
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.utils.Constants

/**
 * Single source of truth for all local persistence.
 * Swap this class with a Firebase-backed implementation later without changing repositories.
 */
class SharedPrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // ── Role ─────────────────────────────────────────────────────────────────

    fun saveRole(role: String) = prefs.edit().putString(Constants.KEY_ROLE, role).apply()
    fun getRole(): String? = prefs.getString(Constants.KEY_ROLE, null)
    fun clearRole() = prefs.edit().remove(Constants.KEY_ROLE).apply()

    // ── Tasks ─────────────────────────────────────────────────────────────────

    fun saveTasks(tasks: List<Task>) {
        prefs.edit().putString(Constants.KEY_TASKS, gson.toJson(tasks)).apply()
    }

    fun getTasks(): MutableList<Task> {
        val json = prefs.getString(Constants.KEY_TASKS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    // ── Game Progress ─────────────────────────────────────────────────────────

    fun saveGameProgress(progress: GameProgress) {
        prefs.edit().putString(Constants.KEY_GAME_PROGRESS, gson.toJson(progress)).apply()
    }

    fun getGameProgress(): GameProgress {
        val json = prefs.getString(Constants.KEY_GAME_PROGRESS, null) ?: return GameProgress()
        return gson.fromJson(json, GameProgress::class.java) ?: GameProgress()
    }

    // ── Bedtime ────────────────────────────────────────────────────────────────

    fun saveBedtimeState(state: BedtimeState) {
        prefs.edit().putString(Constants.KEY_BEDTIME, gson.toJson(state)).apply()
    }

    fun getBedtimeState(): BedtimeState {
        val json = prefs.getString(Constants.KEY_BEDTIME, null) ?: return BedtimeState()
        return gson.fromJson(json, BedtimeState::class.java) ?: BedtimeState()
    }

    // ── First launch ──────────────────────────────────────────────────────────

    fun isFirstLaunch(): Boolean = prefs.getBoolean(Constants.KEY_FIRST_LAUNCH, true)
    fun markLaunched() = prefs.edit().putBoolean(Constants.KEY_FIRST_LAUNCH, false).apply()
}
