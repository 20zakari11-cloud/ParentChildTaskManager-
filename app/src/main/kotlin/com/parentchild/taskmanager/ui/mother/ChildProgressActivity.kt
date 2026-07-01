package com.parentchild.taskmanager.ui.mother

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.data.model.Characters
import com.parentchild.taskmanager.data.model.GameProgress
import com.parentchild.taskmanager.data.repository.ProgressRepository
import com.parentchild.taskmanager.databinding.ActivityChildProgressBinding
import com.parentchild.taskmanager.utils.Constants

class ChildProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "تقدم الطفل"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadProgress()
    }

    override fun onResume() {
        super.onResume()
        loadProgress()
    }

    private fun loadProgress() {
        val progress = ProgressRepository(SharedPrefsManager(this)).getProgress()
        renderProgress(progress)
    }

    private fun renderProgress(p: GameProgress) {
        val character = Characters.getActiveFor(p.level)

        binding.tvCharacterEmoji.text = character.emoji
        binding.tvCharacterName.text = character.nameAr
        binding.tvCurrentTitle.text = p.currentTitle

        binding.tvLevel.text = "المستوى ${p.level}"
        binding.tvStars.text = "⭐ ${p.stars} نجمة إجمالية"
        binding.tvTasksCompleted.text = "المهام المنجزة: ${p.totalTasksCompleted}"

        val starsInLevel = p.stars % GameProgress.STARS_PER_LEVEL
        binding.tvStarsToNext.text =
            if (p.starsToNextLevel == GameProgress.STARS_PER_LEVEL)
                "أنجز ${GameProgress.STARS_PER_LEVEL} مهام للوصول للمستوى التالي"
            else
                "$starsInLevel / ${GameProgress.STARS_PER_LEVEL} نجمات للمستوى التالي"

        binding.progressBar.progress = (p.levelProgressFraction * 100).toInt()

        // Unlocked characters list
        val unlockedText = Characters.all.joinToString("\n") { ch ->
            val locked = ch.id !in p.unlockedCharacterIds
            val mark = if (locked) "🔒" else "✅"
            "$mark ${ch.emoji} ${ch.nameAr} — المستوى ${ch.unlockLevel}"
        }
        binding.tvCharacterList.text = unlockedText
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
