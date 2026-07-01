package com.parentchild.taskmanager.ui.role

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentchild.taskmanager.data.local.SharedPrefsManager
import com.parentchild.taskmanager.databinding.ActivityRoleSelectionBinding
import com.parentchild.taskmanager.ui.child.ChildHomeActivity
import com.parentchild.taskmanager.ui.mother.MotherDashboardActivity
import com.parentchild.taskmanager.utils.Constants
import com.parentchild.taskmanager.utils.startActivity

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding
    private lateinit var prefs: SharedPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPrefsManager(this)

        // If role already selected, skip to the right screen
        prefs.getRole()?.let { role ->
            navigateTo(role)
            return
        }

        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMother.setOnClickListener {
            saveAndNavigate(Constants.ROLE_MOTHER)
        }

        binding.btnChild.setOnClickListener {
            saveAndNavigate(Constants.ROLE_CHILD)
        }
    }

    private fun saveAndNavigate(role: String) {
        prefs.saveRole(role)
        navigateTo(role)
    }

    private fun navigateTo(role: String) {
        when (role) {
            Constants.ROLE_MOTHER -> startActivity<MotherDashboardActivity>()
            Constants.ROLE_CHILD  -> startActivity<ChildHomeActivity>()
        }
        finish()
    }
}
