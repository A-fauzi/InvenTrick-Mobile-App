package com.example.warehouseproject.example_mvp.main

interface ExampleMainView {
    fun showProgress()
    fun hideProgress()
    fun setItems(items: List<String>)
    fun showData(data: String)
}