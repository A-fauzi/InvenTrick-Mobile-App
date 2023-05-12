package com.example.warehouseproject.core.utils.helper

import android.content.Context
import com.example.warehouseproject.domain.modelentities.product.ProductModelAssets
import com.example.warehouseproject.core.utils.DataFromAssets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataJsonFromAssets {
    fun get(context: Context, jsonFileName: String, data: (data: List<ProductModelAssets>) -> Unit) {
        val jsonFileString = DataFromAssets().getJsonDataFromAssets(context, jsonFileName)
        val gson = Gson()
        val listType = object : TypeToken<List<ProductModelAssets>>() {}.type
        val list: List<ProductModelAssets> = gson.fromJson(jsonFileString, listType)

        data(list)

    }
}