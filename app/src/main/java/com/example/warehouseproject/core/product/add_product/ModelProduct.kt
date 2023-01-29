package com.example.warehouseproject.core.product.add_product

data class ModelProduct(
    var image: String,
    val code_items: String,
    val name: String,
    val qty: Int,
    val category: String,
    val sub_category: String,
    val specification: String,
    val price: Int,
    val location: String,
    val status: String,
    val model: String,
    val code_oracle: String,
    val description_oracle: String,
) {
    data class ProductResponse(
        val message: String,
        val count: String,
        val data: List<ModelProduct>
    )
}
