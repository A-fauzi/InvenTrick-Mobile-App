package com.example.warehouseproject.core.model.product

import com.google.gson.annotations.SerializedName

data class ProductModelAssets(

    @SerializedName("ITEM CODE")
    val itemCode: String,

    @SerializedName("ITEM DESC")
    val itemNameDesc: String
)