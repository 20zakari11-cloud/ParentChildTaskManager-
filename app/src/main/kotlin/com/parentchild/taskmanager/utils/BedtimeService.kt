package com.parentchild.taskmanager.utils

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.repository.BedtimeRepository
import com.parentchild.taskmanager.ui.child.BedtimeActivity

/**
 * Monitors bedtime phases and launches BedtimeActivity at the right moment.
 * Firebase migration: replace with FCM push that triggers BedtimeActivity on the child's device.
 */
class BedtimeService : Service() {

    private var timer: CountDownTimer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val repo = BedtimeRepository(SharedPrefsManager(this))
        val state = repo.getState()
        if (!state.isActive) { stopSelf(); return START_NOT_STICKY }

        // Phase 1 reminder: launch immediately
        startActivity(
            Intent(this, BedtimeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("phase", "REMINDER")
            }
        )

        // Phase 2: full bedtime after 5 minutes
        val remaining = (Constants.BEDTIME_REMINDER_MINUTES * 60_000L) -
                (System.currentTimeMillis() - state.activatedAt)

        if (remaining > 0) {
            timer = object : CountDownTimer(remaining, 1000) {
                override fun onTick(ms: Long) {}
                override fun onFinish() {
                    startActivity(
                        Intent(this@BedtimeService, BedtimeActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            putExtra("phase", "FULL_BEDTIME")
                        }
                    )
                    stopSelf()
                }
            }.start()
        } else {
            startActivity(
                Intent(this, BedtimeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("phase", "FULL_BEDTIME")
                }
            )
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
