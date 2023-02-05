package com.example.warehouseproject.core.model.product

data class Product(
    val _id: String,
    val code_items: String,
    val name: String,
    val qty: String,
    val category: String,
    val sub_category: String,
    var image: String,
    val specification: String,
    val price: String,
    val location: String,
    val status: String,
    val model: String,
    val lot: String,
    val exp: String,
    val created_at: String? = null,
    val updated_at: String? = null
)
