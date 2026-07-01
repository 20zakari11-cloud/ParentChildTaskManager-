package com.parentchild.taskmanager.data.repository

import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.model.TaskStatus

/**
 * Abstracts task storage. Replace body with Firebase Firestore calls when ready.
 *
 * Firebase migration path:
 *   - Inject FirebaseFirestore here alongside SharedPrefsManager
 *   - Write to Firestore in all mutating methods
 *   - Return Flow<List<Task>> from getTasks() using snapshotFlow
 */
class TaskRepository(private val prefs: SharedPrefsManager) {

    fun getAllTasks(): List<Task> = prefs.getTasks()

    fun getPendingTask(): Task? = prefs.getTasks()
        .firstOrNull { it.status == TaskStatus.PENDING || it.status == TaskStatus.SNOOZED }

    fun addTask(task: Task) {
        val tasks = prefs.getTasks().also { it.add(task) }
        prefs.saveTasks(tasks)
    }

    fun updateTask(updated: Task) {
        val tasks = prefs.getTasks()
        val idx = tasks.indexOfFirst { it.id == updated.id }
        if (idx != -1) {
            tasks[idx] = updated
            prefs.saveTasks(tasks)
        }
    }

    fun deleteTask(taskId: String) {
        val tasks = prefs.getTasks().filter { it.id != taskId }.toMutableList()
        prefs.saveTasks(tasks)
    }

    fun markCompleted(taskId: String): Task? {
        val tasks = prefs.getTasks()
        val idx = tasks.indexOfFirst { it.id == taskId }
        if (idx == -1) return null
        val updated = tasks[idx].copy(
            status = TaskStatus.COMPLETED,
            completedAt = System.currentTimeMillis()
        )
        tasks[idx] = updated
        prefs.saveTasks(tasks)
        return updated
    }

    fun snoozeTask(taskId: String, snoozeUntil: Long): Task? {
        val tasks = prefs.getTasks()
        val idx = tasks.indexOfFirst { it.id == taskId }
        if (idx == -1) return null
        val task = tasks[idx]
        if (task.snoozeUsed) return null
        val updated = task.copy(
            status = TaskStatus.SNOOZED,
            snoozeUsed = true,
            snoozedAt = System.currentTimeMillis(),
            snoozeUntil = snoozeUntil
        )
        tasks[idx] = updated
        prefs.saveTasks(tasks)
        return updated
    }

    fun wakeFromSnooze(taskId: String) {
        val tasks = prefs.getTasks()
        val idx = tasks.indexOfFirst { it.id == taskId }
        if (idx == -1) return
        val updated = tasks[idx].copy(status = TaskStatus.PENDING, snoozeUntil = null)
        tasks[idx] = updated
        prefs.saveTasks(tasks)
    }
}
