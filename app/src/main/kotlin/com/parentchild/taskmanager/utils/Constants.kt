package com.parentchild.taskmanager.utils

object Constants {
    const val PREFS_NAME = "parent_child_prefs"

    const val KEY_ROLE = "user_role"
    const val KEY_TASKS = "tasks"
    const val KEY_GAME_PROGRESS = "game_progress"
    const val KEY_BEDTIME = "bedtime_state"
    const val KEY_FIRST_LAUNCH = "first_launch"

    const val ROLE_MOTHER = "MOTHER"
    const val ROLE_CHILD  = "CHILD"

    const val SNOOZE_DURATION_MS = 10 * 60 * 1000L  // 10 minutes

    const val BEDTIME_REMINDER_MINUTES = 5L
    const val BEDTIME_FULL_MINUTES     = 5L  // after reminder

    const val EXTRA_TASK_ID         = "extra_task_id"
    const val EXTRA_LEVELED_UP      = "extra_leveled_up"
    const val EXTRA_NEW_LEVEL       = "extra_new_level"

    const val ACTION_SNOOZE_ENDED   = "com.parentchild.taskmanager.SNOOZE_ENDED"
    const val ACTION_BEDTIME_PHASE  = "com.parentchild.taskmanager.BEDTIME_PHASE"

    const val NOTIFICATION_CHANNEL_TASKS   = "tasks_channel"
    const val NOTIFICATION_CHANNEL_BEDTIME = "bedtime_channel"
    const val NOTIFICATION_ID_SNOOZE       = 1001
    const val NOTIFICATION_ID_BEDTIME      = 1002

    const val REQUEST_CODE_SNOOZE  = 2001
    const val REQUEST_CODE_BEDTIME = 2002
}
