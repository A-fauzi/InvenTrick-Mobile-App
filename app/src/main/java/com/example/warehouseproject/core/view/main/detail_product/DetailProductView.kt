package com.example.warehouseproject.core.view.main.detail_product

import com.example.warehouseproject.core.model.product.Product

interface DetailProductView {
    fun onResponseSuccessBody(msg: String, data: Product?)
    fun onResponseErrorBody(msg: String)
    fun onFailureDeleteProduct(msg: String)
}