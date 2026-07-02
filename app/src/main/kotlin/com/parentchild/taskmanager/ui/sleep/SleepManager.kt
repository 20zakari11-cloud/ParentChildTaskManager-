package com.parentchild.taskmanager.ui.sleep

import android.content.Context
import android.content.Intent
import com.parentchild.taskmanager.ui.overlay.TaskOverlayActivity

object SleepManager {

    fun triggerSleep(context: Context) {
        val intent = Intent(context, TaskOverlayActivity::class.java)
        intent.putExtra("title", "حان وقت النوم 🌙")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
