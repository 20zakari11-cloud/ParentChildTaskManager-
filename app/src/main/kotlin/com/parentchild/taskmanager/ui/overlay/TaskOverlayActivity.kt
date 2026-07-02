package com.parentchild.taskmanager.ui.overlay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.databinding.ActivityTaskOverlayBinding

class TaskOverlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskOverlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityTaskOverlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: ""

        binding.taskTitle.text = title

        binding.btnSnooze.setOnClickListener {
            // تأجيل 10 دقائق مرة واحدة
            finish()
        }

        binding.btnDone.setOnClickListener {
            // إكمال المهمة
            finish()
        }
    }
}
