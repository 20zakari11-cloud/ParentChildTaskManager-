package com.parentchild.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.repository.BedtimeRepository
import com.parentchild.taskmanager.data.repository.ProgressRepository
import com.parentchild.taskmanager.data.repository.TaskRepository

class MotherViewModelFactory(private val prefs: SharedPrefsManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MotherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MotherViewModel(
                TaskRepository(prefs),
                ProgressRepository(prefs),
                BedtimeRepository(prefs)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
