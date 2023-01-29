package com.example.warehouseproject.example_mvp.main

import com.example.warehouseproject.example_mvp.postDelayed

class ExampleFindItemInteractor {
    fun findItems(callback: (List<String>) -> Unit) {
        postDelayed(2000) {callback(createArrayList())}
    }

    private fun createArrayList(): List<String> = (1..100).map { "Items $it" }
}