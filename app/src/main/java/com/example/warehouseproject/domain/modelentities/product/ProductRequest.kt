package com.example.warehouseproject.domain.modelentities.product

import com.example.warehouseproject.domain.modelentities.user.User

data class ProductRequest(
    var image: String? = null,
    val code_items: String? = null,
    val name: String? = null,
    val user: User? = null,
    val qty: String? = null,
    val category: String? = null,
    val sub_category: String? = null,
    val specification: String? = null,
    var price: Int? = null,
    val location: String? = null,
    val status: String? = null,
    val model: String? = null,
    val lot: String? = null,
    val exp: String? = null,
    var path_storage: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

