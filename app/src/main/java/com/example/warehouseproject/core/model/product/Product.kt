package com.example.warehouseproject.core.model.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val _id: String,

    @ColumnInfo(name = "code_item")
    val code_items: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "qty")
    val qty: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "sub_category")
    val sub_category: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "specification")
    val specification: String,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "lot")
    val lot: String,

    @ColumnInfo(name = "exp")
    val exp: String,

    @ColumnInfo(name = "created_at")
    val created_at: String? = null,

    @ColumnInfo(name = "updated_at")
    val updated_at: String? = null
)
