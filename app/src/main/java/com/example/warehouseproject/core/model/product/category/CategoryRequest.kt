package com.example.warehouseproject.core.model.product.category

data class CategoryRequest(
    val name: String,
    val sub_category: List<Category.SubCategory>,
) {
    data class SubCategoryRequest(
        val name: String,
        val created_at: String
    )
}