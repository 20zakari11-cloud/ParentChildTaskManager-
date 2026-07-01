package com.parentchild.taskmanager.ui.child

import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.BedtimePhase
import com.parentchild.taskmanager.data.repository.BedtimeRepository
import com.parentchild.taskmanager.databinding.ActivityBedtimeBinding
import com.parentchild.taskmanager.utils.gone
import com.parentchild.taskmanager.utils.toTimeString
import com.parentchild.taskmanager.utils.visible

class BedtimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBedtimeBinding
    private var phaseTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBedtimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Keep screen on during bedtime mode
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val phaseStr = intent.getStringExtra("phase") ?: "REMINDER"
        val phase = BedtimePhase.valueOf(phaseStr)
        applyPhase(phase)

        // Allow mother to dismiss (password in real app)
        binding.btnDismiss.setOnClickListener {
            BedtimeRepository(SharedPrefsManager(this)).deactivate()
            finish()
        }
    }

    private fun applyPhase(phase: BedtimePhase) {
        when (phase) {
            BedtimePhase.REMINDER -> showReminderPhase()
            BedtimePhase.FULL_BEDTIME -> showFullBedtimePhase()
            BedtimePhase.INACTIVE -> finish()
        }
    }

    private fun showReminderPhase() {
        binding.layoutReminder.visible()
        binding.layoutFullBedtime.gone()

        val totalMs = 5 * 60 * 1000L
        phaseTimer = object : CountDownTimer(totalMs, 1000L) {
            override fun onTick(ms: Long) {
                binding.tvReminderCountdown.text = "⏱ ${ms.toTimeString()}"
            }
            override fun onFinish() {
                showFullBedtimePhase()
            }
        }.start()
    }

    private fun showFullBedtimePhase() {
        phaseTimer?.cancel()
        binding.layoutReminder.gone()
        binding.layoutFullBedtime.visible()

        // Dark overlay — already handled by dark theme in XML
        binding.tvSleepMessage.text = "وقت النوم يا صغير 🌙\nأغمض عينيك واسترح"
    }

    override fun onBackPressed() {
        // Prevent back press during bedtime
        val state = BedtimeRepository(SharedPrefsManager(this)).getState()
        if (!state.isActive) {
            super.onBackPressed()
        }
        // else: silently block back press
    }

    override fun onDestroy() {
        phaseTimer?.cancel()
        super.onDestroy()
    }
}
