package com.parentchild.taskmanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.parentchild.taskmanager.R
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.repository.TaskRepository
import com.parentchild.taskmanager.ui.child.ChildHomeActivity

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getStringExtra(Constants.EXTRA_TASK_ID) ?: return
        val repo = TaskRepository(SharedPrefsManager(context))
        repo.wakeFromSnooze(taskId)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_TASKS,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(ch)
        }

        val tap = PendingIntent.getActivity(
            context, 0,
            Intent(context, ChildHomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_TASKS)
            .setSmallIcon(R.drawable.ic_star)
            .setContentTitle("⏰ مهمتك جاهزة!")
            .setContentText("انتهت فترة التأجيل. حان وقت إنجاز المهمة!")
            .setContentIntent(tap)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        nm.notify(Constants.NOTIFICATION_ID_SNOOZE, notif)
    }
}
