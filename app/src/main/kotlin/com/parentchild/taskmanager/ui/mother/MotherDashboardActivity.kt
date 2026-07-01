package com.parentchild.taskmanager.ui.mother

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.databinding.ActivityMotherDashboardBinding
import com.parentchild.taskmanager.ui.role.RoleSelectionActivity
import com.parentchild.taskmanager.utils.Constants
import com.parentchild.taskmanager.utils.gone
import com.parentchild.taskmanager.utils.startActivity
import com.parentchild.taskmanager.utils.toast
import com.parentchild.taskmanager.utils.visible
import com.parentchild.taskmanager.viewmodel.MotherViewModel
import com.parentchild.taskmanager.viewmodel.MotherViewModelFactory

class MotherDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMotherDashboardBinding
    private val vm: MotherViewModel by viewModels {
        MotherViewModelFactory(SharedPrefsManager(this))
    }
    private lateinit var taskAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddTask.setOnClickListener {
            startActivityForResult(
                Intent(this, AddTaskActivity::class.java),
                REQUEST_ADD_TASK
            )
        }

        binding.btnViewProgress.setOnClickListener {
            startActivity<ChildProgressActivity>()
        }

        binding.btnBedtime.setOnClickListener {
            toggleBedtime()
        }

        binding.btnSwitchRole.setOnClickListener {
            SharedPrefsManager(this).clearRole()
            startActivity<RoleSelectionActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        vm.loadAll()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskListAdapter(
            onComplete = { task -> confirmComplete(task) },
            onDelete   = { task -> vm.deleteTask(task.id) }
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(this@MotherDashboardActivity)
            adapter = taskAdapter
        }
    }

    private fun observeViewModel() {
        vm.tasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
            if (tasks.isEmpty()) {
                binding.tvEmptyState.visible()
                binding.rvTasks.gone()
            } else {
                binding.tvEmptyState.gone()
                binding.rvTasks.visible()
            }
        }

        vm.bedtime.observe(this) { state ->
            binding.btnBedtime.text = if (state.isActive) "🌙 إلغاء وضع النوم" else "🌙 تفعيل وضع النوم"
        }

        vm.progress.observe(this) { progress ->
            binding.tvStarCount.text = "⭐ ${progress.stars} نجمة"
            binding.tvLevel.text = "المستوى ${progress.level}"
        }
    }

    private fun confirmComplete(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("تأكيد إتمام المهمة")
            .setMessage("هل تريد تحديد «${task.title}» كمكتملة؟\nسيحصل الطفل على نجمة ⭐")
            .setPositiveButton("نعم") { _, _ ->
                vm.markCompleted(task.id)
                toast("تم! حصل الطفل على نجمة ⭐")
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun toggleBedtime() {
        val state = vm.bedtime.value
        if (state?.isActive == true) {
            vm.deactivateBedtime()
            toast("تم إلغاء وضع النوم")
        } else {
            vm.activateBedtime()
            startService(Intent(this, com.parentchild.taskmanager.utils.BedtimeService::class.java))
            toast("تم تفعيل وضع النوم 🌙")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_TASK && resultCode == RESULT_OK) {
            vm.loadAll()
        }
    }

    companion object {
        private const val REQUEST_ADD_TASK = 100
    }
}
