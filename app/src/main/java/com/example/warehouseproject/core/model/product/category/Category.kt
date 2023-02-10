package com.example.warehouseproject.core.model.product.category

data class Category(
    val _id: String,
    val name: String,
    val sub_category: List<SubCategory>,
    val created_at: String,
    val updated_at: String
) {
    data class SubCategory(
        val _id: String,
        val name: String,
        val created_at: String
    )
}
