package com.parentchild.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parentchild.taskmanager.data.model.BedtimeState
import com.parentchild.taskmanager.data.model.GameProgress
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.repository.BedtimeRepository
import com.parentchild.taskmanager.data.repository.ProgressRepository
import com.parentchild.taskmanager.data.repository.TaskRepository

class MotherViewModel(
    private val taskRepo: TaskRepository,
    private val progressRepo: ProgressRepository,
    private val bedtimeRepo: BedtimeRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _progress = MutableLiveData<GameProgress>()
    val progress: LiveData<GameProgress> get() = _progress

    private val _bedtime = MutableLiveData<BedtimeState>()
    val bedtime: LiveData<BedtimeState> get() = _bedtime

    fun loadAll() {
        _tasks.value = taskRepo.getAllTasks()
        _progress.value = progressRepo.getProgress()
        _bedtime.value = bedtimeRepo.getState()
    }

    fun addTask(task: Task) {
        taskRepo.addTask(task)
        _tasks.value = taskRepo.getAllTasks()
    }

    fun markCompleted(taskId: String) {
        taskRepo.markCompleted(taskId)
        val (newProgress, _) = progressRepo.addStar()
        _tasks.value = taskRepo.getAllTasks()
        _progress.value = newProgress
    }

    fun deleteTask(taskId: String) {
        taskRepo.deleteTask(taskId)
        _tasks.value = taskRepo.getAllTasks()
    }

    fun activateBedtime(): BedtimeState {
        val state = bedtimeRepo.activate()
        _bedtime.value = state
        return state
    }

    fun deactivateBedtime(): BedtimeState {
        val state = bedtimeRepo.deactivate()
        _bedtime.value = state
        return state
    }
}
