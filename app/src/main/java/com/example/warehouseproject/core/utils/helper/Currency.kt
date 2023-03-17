package com.example.warehouseproject.core.utils.helper

import java.text.NumberFormat
import java.util.*

object Currency {
    fun format(number: Double, lang: String, country: String): String {
        val localId: Locale = Locale(lang, country)
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localId)
        return formatRupiah.format(number)
    }
}