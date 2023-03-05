package com.example.warehouseproject.core.view.main.home_fragment

import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductResponses

interface HomeView {
    fun moveMainActivity()
    fun successResponseBodyGetProductsView(data: List<Product>, productResponses: ProductResponses)
    fun errorResponseBodyGetProductsView(msg: String)
    fun onFailureRequestGetProductsView(msg: String)
}