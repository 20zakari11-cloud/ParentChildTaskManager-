package com.parentchild.taskmanager.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.databinding.ActivityRoleSelectionBinding
import com.parentchild.taskmanager.ui.mother.MotherDashboardActivity
import com.parentchild.taskmanager.ui.child.ChildHomeActivity

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnParent.setOnClickListener {
            startActivity(Intent(this, MotherDashboardActivity::class.java))
            finish()
        }

        binding.btnChild.setOnClickListener {
            startActivity(Intent(this, ChildHomeActivity::class.java))
            finish()
        }
    }
}
