package com.parentchild.taskmanager.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.BedtimePhase
import com.parentchild.taskmanager.data.model.BedtimeState
import com.parentchild.taskmanager.data.model.GameProgress
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.model.TaskStatus
import com.parentchild.taskmanager.data.repository.BedtimeRepository
import com.parentchild.taskmanager.data.repository.ProgressRepository
import com.parentchild.taskmanager.data.repository.TaskRepository
import com.parentchild.taskmanager.utils.Constants

class ChildViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = SharedPrefsManager(app)
    private val taskRepo = TaskRepository(prefs)
    private val progressRepo = ProgressRepository(prefs)
    private val bedtimeRepo = BedtimeRepository(prefs)

    private val _currentTask = MutableLiveData<Task?>()
    val currentTask: LiveData<Task?> get() = _currentTask

    private val _progress = MutableLiveData<GameProgress>()
    val progress: LiveData<GameProgress> get() = _progress

    private val _bedtime = MutableLiveData<BedtimeState>()
    val bedtime: LiveData<BedtimeState> get() = _bedtime

    private val _snoozeCountdown = MutableLiveData<Long>()
    val snoozeCountdown: LiveData<Long> get() = _snoozeCountdown

    private val _levelUpEvent = MutableLiveData<Int?>()
    val levelUpEvent: LiveData<Int?> get() = _levelUpEvent

    private var snoozeTimer: CountDownTimer? = null

    fun load() {
        refreshTask()
        _progress.value = progressRepo.getProgress()
        _bedtime.value = bedtimeRepo.getState()
    }

    fun refreshTask() {
        val task = taskRepo.getPendingTask()
        _currentTask.value = task

        // Resume snooze countdown if task is snoozed
        if (task?.status == TaskStatus.SNOOZED && task.snoozeUntil != null) {
            val remaining = task.snoozeUntil!! - System.currentTimeMillis()
            if (remaining > 0) startSnoozeTimer(task.id, remaining)
            else {
                taskRepo.wakeFromSnooze(task.id)
                refreshTask()
            }
        }
    }

    fun completeTask(taskId: String) {
        snoozeTimer?.cancel()
        taskRepo.markCompleted(taskId)
        val (newProgress, leveledUp) = progressRepo.addStar()
        _progress.value = newProgress
        if (leveledUp) _levelUpEvent.value = newProgress.level
        _currentTask.value = taskRepo.getPendingTask()
    }

    fun snoozeTask(taskId: String): Boolean {
        val task = taskRepo.getPendingTask() ?: return false
        if (task.snoozeUsed) return false
        val until = System.currentTimeMillis() + Constants.SNOOZE_DURATION_MS
        taskRepo.snoozeTask(taskId, until)
        startSnoozeTimer(taskId, Constants.SNOOZE_DURATION_MS)
        refreshTask()
        return true
    }

    private fun startSnoozeTimer(taskId: String, durationMs: Long) {
        snoozeTimer?.cancel()
        snoozeTimer = object : CountDownTimer(durationMs, 1000L) {
            override fun onTick(ms: Long) { _snoozeCountdown.value = ms }
            override fun onFinish() {
                taskRepo.wakeFromSnooze(taskId)
                _snoozeCountdown.value = 0L
                refreshTask()
            }
        }.start()
    }

    fun clearLevelUpEvent() { _levelUpEvent.value = null }

    override fun onCleared() {
        snoozeTimer?.cancel()
        super.onCleared()
    }
}
