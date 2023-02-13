package com.example.warehouseproject.core.utils

import android.content.Context
import java.io.IOException

class DataFromAssets {
    fun getJsonDataFromAssets(context: Context, fillName: String): String? {
        val jsonString: String

        try {
            jsonString = context.assets.open(fillName).bufferedReader().use { it.readText() }
        }catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }

        return jsonString
    }
}