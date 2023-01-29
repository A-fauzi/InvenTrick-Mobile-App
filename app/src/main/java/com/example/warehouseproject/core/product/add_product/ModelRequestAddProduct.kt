package com.example.warehouseproject.core.product.add_product

data class ModelRequestAddProduct(
    val code_items: String,
    val name: String,
    val category: String,
    val sub_category: String,
    val image: String,
    val specification: String,
    val price: String,
    val location: String,
    val status: String,
    val model: String,
    val code_oracle: String,
    val description_oracle: String,
)
