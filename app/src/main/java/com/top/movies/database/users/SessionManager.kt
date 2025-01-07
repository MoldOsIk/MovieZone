package com.top.movies.database.users

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USERNAME = "username"
    }

    fun saveUserSession(username: String) {
        preferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    fun getUserSession(): String? {
        val isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
        return if (isLoggedIn) {
            preferences.getString(KEY_USERNAME, null)
        } else {
            null
        }
    }

    fun clearSession() {
        preferences.edit().clear().apply()
    }
}
