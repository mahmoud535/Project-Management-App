package com.example.managementapp.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferenceManager(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("github_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("github_token", null)
    }
}