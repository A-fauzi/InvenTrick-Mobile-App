package com.example.warehouseproject.core.view.main.home_fragment

import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.domain.modelentities.product.response.ProductResponses
import com.example.warehouseproject.core.service.product.ProductApiService


class HomePresenter(
    private val mainView: HomeView?,
    private val productApiService: ProductApiService
    ): ProductApiService.OnFinishedGetProducts {

    fun getDataProducts(page: Int = 1) {
        productApiService.getDataProduct(page, this)
    }

    override fun successResponseBodyGetProducts(data: List<Product>, productResponses: ProductResponses) {
        mainView?.successResponseBodyGetProductsView(data, productResponses)
    }

    override fun errorResponseBodyGetProducts(msg: String) {
        mainView?.errorResponseBodyGetProductsView(msg)
    }

    override fun onFailureRequestGetProducts(msg: String) {
        mainView?.onFailureRequestGetProductsView(msg)
    }
}