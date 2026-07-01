package com.parentchild.taskmanager.data.model

data class BedtimeState(
    val isActive: Boolean = false,
    val activatedAt: Long = 0L
) {
    val minutesElapsed: Long
        get() = if (isActive) (System.currentTimeMillis() - activatedAt) / 60_000L else 0L

    val phase: BedtimePhase
        get() = when {
            !isActive -> BedtimePhase.INACTIVE
            minutesElapsed < 5 -> BedtimePhase.REMINDER
            else -> BedtimePhase.FULL_BEDTIME
        }
}

enum class BedtimePhase {
    INACTIVE,
    REMINDER,
    FULL_BEDTIME
}
