package com.parentchild.taskmanager.data.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    var status: TaskStatus = TaskStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    var completedAt: Long? = null,
    var snoozedAt: Long? = null,
    var snoozeUsed: Boolean = false,
    var snoozeUntil: Long? = null
)

enum class TaskStatus {
    PENDING,
    SNOOZED,
    COMPLETED
}
