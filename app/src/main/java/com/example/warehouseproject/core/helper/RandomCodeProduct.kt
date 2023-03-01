package com.example.warehouseproject.core.helper

object RandomCodeProduct {
    fun generate(): String {
        val alphabet = ('a'..'z') + ('A'..'Z') // alfabet yang tersedia
        val numbers = ('0'..'9') // angka yang tersedia
        return (1..8)
            .map<Int, Any> { if (it <= 4) alphabet.random().uppercase() else numbers.random() }
            .joinToString("")
    }
}