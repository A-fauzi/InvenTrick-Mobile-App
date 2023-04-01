package com.example.warehouseproject.core.model.product.category

data class Category(
    val _id: String? = null,
    val name: String? = null,
    val sub_category: List<SubCategory>? = null,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    data class SubCategory(
        val _id: String? = null,
        val name: String? = null,
        val created_at: String? = null
    )
}
