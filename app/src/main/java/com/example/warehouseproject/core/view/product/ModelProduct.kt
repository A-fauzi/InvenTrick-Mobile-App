package com.example.warehouseproject.core.view.product

data class ModelProduct(
    var image: String,
    val code_items: String,
    val name: String,
    val qty: String,
    val category: String,
    val sub_category: String,
    val specification: String,
    val price: String,
    val location: String,
    val status: String,
    val model: String,
    val code_oracle: String,
    val description_oracle: String,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    data class ProductResponse(
        val message: String,
        val count: String,
        val data: List<ModelProduct>
    )
    data class ProductSingleResponse(
        val message: String,
        val data: ModelProduct
    )
}
