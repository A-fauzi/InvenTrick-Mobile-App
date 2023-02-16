package com.example.warehouseproject.core.model.product

import com.example.warehouseproject.core.model.user.User

data class ProductRequest(
    var image: String,
    val code_items: String,
    val name: String,
    val user: User,
    val qty: String,
    val category: String,
    val sub_category: String,
    val specification: String,
    var price: String,
    val location: String,
    val status: String,
    val model: String,
    val lot: String,
    val exp: String,
    var path_storage: String,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    data class RequestQtyOnly(
        val qty: String
    )
}

