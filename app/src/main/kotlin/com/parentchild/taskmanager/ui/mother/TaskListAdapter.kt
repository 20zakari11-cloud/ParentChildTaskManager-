package com.parentchild.taskmanager.ui.mother

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parentchild.taskmanager.R
import com.parentchild.taskmanager.data.model.Task
import com.parentchild.taskmanager.data.model.TaskStatus
import com.parentchild.taskmanager.databinding.ItemTaskBinding
import com.parentchild.taskmanager.utils.gone
import com.parentchild.taskmanager.utils.toReadableDate
import com.parentchild.taskmanager.utils.visible

class TaskListAdapter(
    private val onComplete: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DIFF) {

    inner class TaskViewHolder(private val b: ItemTaskBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(task: Task) {
            b.tvTitle.text = task.title
            b.tvDescription.text = task.description
            b.tvDate.text = task.createdAt.toReadableDate()

            when (task.status) {
                TaskStatus.PENDING   -> {
                    b.tvStatus.text = "⏳ قيد الانتظار"
                    b.tvStatus.setTextColor(ContextCompat.getColor(b.root.context, R.color.status_pending))
                    b.btnComplete.visible()
                }
                TaskStatus.SNOOZED   -> {
                    b.tvStatus.text = "💤 مؤجل"
                    b.tvStatus.setTextColor(ContextCompat.getColor(b.root.context, R.color.status_snoozed))
                    b.btnComplete.gone()
                }
                TaskStatus.COMPLETED -> {
                    b.tvStatus.text = "✅ مكتملة"
                    b.tvStatus.setTextColor(ContextCompat.getColor(b.root.context, R.color.status_completed))
                    b.btnComplete.gone()
                }
            }

            b.btnComplete.setOnClickListener { onComplete(task) }
            b.btnDelete.setOnClickListener   { onDelete(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(a: Task, b: Task) = a.id == b.id
            override fun areContentsTheSame(a: Task, b: Task) = a == b
        }
    }
}
