package com.parentchild.taskmanager.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object AlarmHelper {

    fun scheduleSnoozeAlarm(context: Context, taskId: String, triggerAtMs: Long) {
        val intent = Intent(context, SnoozeReceiver::class.java).apply {
            action = Constants.ACTION_SNOOZE_ENDED
            putExtra(Constants.EXTRA_TASK_ID, taskId)
        }
        val pi = PendingIntent.getBroadcast(
            context,
            Constants.REQUEST_CODE_SNOOZE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMs, pi)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMs, pi)
        }
    }

    fun cancelSnoozeAlarm(context: Context) {
        val intent = Intent(context, SnoozeReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            Constants.REQUEST_CODE_SNOOZE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pi)
    }
}
