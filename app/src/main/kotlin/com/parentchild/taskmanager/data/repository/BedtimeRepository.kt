package com.parentchild.taskmanager.data.repository

import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.BedtimeState

class BedtimeRepository(private val prefs: SharedPrefsManager) {

    fun getState(): BedtimeState = prefs.getBedtimeState()

    fun activate(): BedtimeState {
        val state = BedtimeState(isActive = true, activatedAt = System.currentTimeMillis())
        prefs.saveBedtimeState(state)
        return state
    }

    fun deactivate(): BedtimeState {
        val state = BedtimeState(isActive = false)
        prefs.saveBedtimeState(state)
        return state
    }
}
