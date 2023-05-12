package com.example.warehouseproject.core.view.product.add_product

import com.example.warehouseproject.domain.modelentities.product.Product

interface AddProductView {
    fun onResponseSuccessBodyAddProduct(msg: String, data: Product?)
    fun onResponseErrorBodyAddProduct(msg: String)
    fun onFailureResponseAddProduct(msg: String)
}