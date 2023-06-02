package com.example.warehouseproject.core.view.main.home_fragment

import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.domain.modelentities.product.response.ProductResponses

interface HomeView {
    fun moveMainActivity()
    fun successResponseBodyGetProductsView(data: List<Product>, productResponses: ProductResponses)
    fun errorResponseBodyGetProductsView(msg: String)
    fun onFailureRequestGetProductsView(msg: String)
}