package com.example.warehouseproject.domain.modelentities.product.category

data class CategoryResponse(
    val message: String,
    val count: String,
    val data: List<Category>
){
    data class SingleResponse(
        val message: String,
        val count: String,
        val data: Category
    )
}
