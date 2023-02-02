package com.example.warehouseproject.core.model.product

data class ProductResponses(
    val message: String,
    val count: String,
    val data: List<Product>
) {
    data class SingleResponse(
        val message: String,
        val count: String,
        val data: Product
    )
}
