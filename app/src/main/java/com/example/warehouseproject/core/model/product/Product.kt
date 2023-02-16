package com.example.warehouseproject.core.model.product

import com.example.warehouseproject.core.model.user.User

data class Product(


    val _id: String,

    val code_items: String,

    val user: User,

    val name: String,

    val qty: String,


    val category: String,


    val sub_category: String,


    var image: String,


    val specification: String,


    val price: String,


    val location: String,


    val status: String,


    val model: String,


    val lot: String,


    val exp: String,

    val path_storage: String,


    val created_at: String? = null,


    val updated_at: String? = null
)
