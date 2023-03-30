package com.example.warehouseproject.core.view.product.add_product

import android.content.Intent
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.model.product.category.Category

interface AddProductView {
    fun onResponseSuccessBodyAddProduct(msg: String, data: Product?)
    fun onResponseErrorBodyAddProduct(msg: String)
    fun onFailureResponseAddProduct(msg: String)
}