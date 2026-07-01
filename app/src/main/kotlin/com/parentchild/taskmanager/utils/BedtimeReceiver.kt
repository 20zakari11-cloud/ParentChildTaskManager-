package com.parentchild.taskmanager.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.parentchild.taskmanager.ui.child.BedtimeActivity

class BedtimeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity(
            Intent(context, BedtimeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("phase", intent.getStringExtra("phase") ?: "REMINDER")
            }
        )
    }
}
