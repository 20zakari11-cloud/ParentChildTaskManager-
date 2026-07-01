package com.parentchild.taskmanager.ui.child

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.data.model.Characters
import com.parentchild.taskmanager.data.model.TaskStatus
import com.parentchild.taskmanager.databinding.ActivityChildHomeBinding
import com.parentchild.taskmanager.ui.role.RoleSelectionActivity
import com.parentchild.taskmanager.utils.gone
import com.parentchild.taskmanager.utils.startActivity
import com.parentchild.taskmanager.utils.toast
import com.parentchild.taskmanager.utils.toTimeString
import com.parentchild.taskmanager.utils.visible
import com.parentchild.taskmanager.viewmodel.ChildViewModel

class ChildHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildHomeBinding
    private val vm: ChildViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.btnCompleteTask.setOnClickListener {
            val taskId = vm.currentTask.value?.id ?: return@setOnClickListener
            vm.completeTask(taskId)
        }

        binding.btnSnooze.setOnClickListener {
            val taskId = vm.currentTask.value?.id ?: return@setOnClickListener
            val snoozed = vm.snoozeTask(taskId)
            if (!snoozed) toast("لقد استخدمت التأجيل مسبقاً لهذه المهمة!")
        }

        binding.btnSwitchRole.setOnClickListener {
            startActivity<RoleSelectionActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        vm.load()
    }

    private fun observeViewModel() {
        vm.currentTask.observe(this) { task ->
            if (task == null) {
                binding.cardTask.gone()
                binding.tvNoTask.visible()
                binding.btnCompleteTask.gone()
                binding.btnSnooze.gone()
                binding.layoutSnoozeTimer.gone()
            } else {
                binding.tvNoTask.gone()
                binding.cardTask.visible()
                binding.tvTaskTitle.text = task.title
                binding.tvTaskDescription.text = task.description

                when (task.status) {
                    TaskStatus.SNOOZED -> {
                        binding.btnCompleteTask.gone()
                        binding.btnSnooze.gone()
                        binding.layoutSnoozeTimer.visible()
                    }
                    else -> {
                        binding.btnCompleteTask.visible()
                        binding.btnSnooze.visibility = if (task.snoozeUsed)
                            android.view.View.GONE else android.view.View.VISIBLE
                        binding.layoutSnoozeTimer.gone()
                    }
                }
            }
        }

        vm.progress.observe(this) { p ->
            binding.tvStars.text = "⭐ ${p.stars}"
            binding.tvLevel.text = "المستوى ${p.level}"
            binding.tvTitle.text = p.currentTitle
            binding.progressLevel.progress = (p.levelProgressFraction * 100).toInt()
            val char = Characters.getActiveFor(p.level)
            binding.tvCharacterEmoji.text = char.emoji
        }

        vm.snoozeCountdown.observe(this) { ms ->
            binding.tvSnoozeTimer.text = "⏱ ${ms.toTimeString()}"
        }

        vm.levelUpEvent.observe(this) { level ->
            level ?: return@observe
            showLevelUpDialog(level)
            vm.clearLevelUpEvent()
        }
    }

    private fun showLevelUpDialog(level: Int) {
        val char = Characters.getActiveFor(level)
        val title = com.parentchild.taskmanager.data.model.Titles.forLevel(level)
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🎉 ترقية!")
            .setMessage(
                "وصلت إلى المستوى $level!\n\n" +
                "لقبك الجديد: $title\n\n" +
                "شخصيتك الجديدة: ${char.emoji} ${char.nameAr}"
            )
            .setPositiveButton("رائع! 🎊", null)
            .show()
    }
}
