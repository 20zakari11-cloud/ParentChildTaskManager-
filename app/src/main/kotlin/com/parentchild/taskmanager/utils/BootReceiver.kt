package com.parentchild.taskmanager.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.parentchild.taskmanager.ui.overlay.TaskOverlayActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val i = Intent(context, TaskOverlayActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.putExtra("title", "استكمال المهمة")
        context.startActivity(i)
    }
}
