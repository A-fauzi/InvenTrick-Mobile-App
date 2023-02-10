package com.example.warehouseproject.core.helper

import java.text.SimpleDateFormat
import java.util.*

object SimpleDateFormat {
    fun generate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy | HH:mm:ss", Locale("id"))
        return dateFormat.format(Date())
    }
}