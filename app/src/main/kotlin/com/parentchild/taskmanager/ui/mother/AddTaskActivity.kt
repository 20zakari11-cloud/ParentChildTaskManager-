package com.parentchild.taskmanager.ui.mother

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.repository.TaskRepository
import com.parentchild.taskmanager.databinding.ActivityAddTaskBinding
import com.parentchild.taskmanager.utils.toast

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "إضافة مهمة جديدة"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSendTask.setOnClickListener {
            val title = binding.etTitle.text?.toString()?.trim() ?: ""
            val desc  = binding.etDescription.text?.toString()?.trim() ?: ""

            if (title.isEmpty()) {
                binding.tilTitle.error = "يرجى إدخال عنوان المهمة"
                return@setOnClickListener
            }
            binding.tilTitle.error = null

            val task = Task(title = title, description = desc)
            TaskRepository(SharedPrefsManager(this)).addTask(task)
            toast("تم إرسال المهمة للطفل ✅")
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
