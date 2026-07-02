package com.parentchild.taskmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.model.TaskStatus

class TaskRepository(
    private val prefs: SharedPrefsManager,
    private val firestore: FirebaseFirestore
) {

    // =========================
    // LOCAL STORAGE (SharedPrefs)
    // =========================

    fun getAllTasks(): List<Task> = prefs.getTasks()

    fun getPendingTask(): Task? = prefs.getTasks()
        .firstOrNull { it.status == TaskStatus.PENDING || it.status == TaskStatus.SNOOZED }

    fun addTask(task: Task) {
        val tasks = prefs.getTasks().toMutableList()
        tasks.add(task)
        prefs.saveTasks(tasks)
    }

    fun updateTask(updated: Task) {
        val tasks = prefs.getTasks().toMutableList()
        val idx = tasks.indexOfFirst { it.id == updated.id }
        if (idx != -1) {
            tasks[idx] = updated
            prefs.saveTasks(tasks)
        }
    }

    fun deleteTask(taskId: String) {
        val tasks = prefs.getTasks()
            .filter { it.id != taskId }
            .toMutableList()

        prefs.saveTasks(tasks)
    }

    fun markCompleted(taskId: String): Task? {
        val tasks = prefs.getTasks().toMutableList()
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
        val tasks = prefs.getTasks().toMutableList()
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
        val tasks = prefs.getTasks().toMutableList()
        val idx = tasks.indexOfFirst { it.id == taskId }
        if (idx == -1) return

        val updated = tasks[idx].copy(
            status = TaskStatus.PENDING,
            snoozeUntil = null
        )

        tasks[idx] = updated
        prefs.saveTasks(tasks)
    }

    // =========================
    // FIREBASE SYNC (NEW)
    // =========================

    private val families = firestore.collection("families")

    private fun taskCollection(familyCode: String) =
        families.document(familyCode).collection("tasks")

    fun addTaskRemote(familyCode: String, task: Task) {
        taskCollection(familyCode)
            .document(task.id)
            .set(task)
    }

    fun updateTaskRemote(familyCode: String, task: Task) {
        taskCollection(familyCode)
            .document(task.id)
            .set(task)
    }

    fun deleteTaskRemote(familyCode: String, taskId: String) {
        taskCollection(familyCode)
            .document(taskId)
            .delete()
    }

    fun markCompletedRemote(familyCode: String, task: Task) {
        taskCollection(familyCode)
            .document(task.id)
            .set(task.copy(status = TaskStatus.COMPLETED))
    }

    fun listenTasks(
        familyCode: String,
        onUpdate: (List<Task>) -> Unit
    ) {
        taskCollection(familyCode)
            .addSnapshotListener { snap, _ ->
                val tasks = snap?.documents?.mapNotNull {
                    it.toObject(Task::class.java)
                } ?: emptyList()

                onUpdate(tasks)
            }
    }

    // =========================
    // HYBRID (LOCAL + REMOTE)
    // =========================

    fun addTaskBoth(familyCode: String, task: Task) {
        addTask(task)               // local
        addTaskRemote(familyCode, task) // cloud
    }

    fun updateTaskBoth(familyCode: String, task: Task) {
        updateTask(task)
        updateTaskRemote(familyCode, task)
    }

    fun deleteTaskBoth(familyCode: String, taskId: String) {
        deleteTask(taskId)
        deleteTaskRemote(familyCode, taskId)
    }
}
