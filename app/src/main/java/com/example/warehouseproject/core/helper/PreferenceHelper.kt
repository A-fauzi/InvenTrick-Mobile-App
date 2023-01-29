package com.example.warehouseproject.core.helper

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private lateinit var sharedPreferences: SharedPreferences

    fun saveData(context: Context, value: String, putStrKey: String = "PATH_STORAGE_KEY") {
        sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(putStrKey, value)
        }.apply()
    }

    fun loadData(context: Context, getStrKey: String = "PATH_STORAGE_KEY"): String? {
        sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        return sharedPreferences.getString(getStrKey, null)
    }
}