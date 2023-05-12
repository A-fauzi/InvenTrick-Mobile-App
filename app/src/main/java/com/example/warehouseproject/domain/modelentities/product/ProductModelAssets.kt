package com.example.warehouseproject.domain.modelentities.product

import com.google.gson.annotations.SerializedName

data class ProductModelAssets(

    @SerializedName("ITEM CODE")
    val itemCode: String,

    @SerializedName("ITEM DESC")
    val itemNameDesc: String
)