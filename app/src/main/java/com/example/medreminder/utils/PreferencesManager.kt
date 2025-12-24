package com.example.medreminder.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "medreminder_prefs",
        Context.MODE_PRIVATE
    )

    var showCountdown: Boolean
        get() = prefs.getBoolean(KEY_SHOW_COUNTDOWN, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_COUNTDOWN, value).apply()

    var use24HourFormat: Boolean
        get() = prefs.getBoolean(KEY_24_HOUR_FORMAT, false)
        set(value) = prefs.edit().putBoolean(KEY_24_HOUR_FORMAT, value).apply()

    companion object {
        private const val KEY_SHOW_COUNTDOWN = "show_countdown"
        private const val KEY_24_HOUR_FORMAT = "use_24_hour_format"
    }
}

