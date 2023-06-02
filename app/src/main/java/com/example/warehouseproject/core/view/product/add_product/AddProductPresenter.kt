package com.example.warehouseproject.core.view.product.add_product

import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService

class AddProductPresenter(var addProductView: AddProductView?, private val productApiService: ProductApiService): ProductApiService.OnFinishedAddProduct {

    fun addProductRequest(request: Product) {
        productApiService.addProductApiService(request, this)
    }

    override fun onResponseSuccessBodyAddProduct(msg: String, data: Product?) {
        addProductView?.onResponseSuccessBodyAddProduct(msg, data)
    }

    override fun onResponseErrorBodyAddProduct(msg: String) {
        addProductView?.onResponseErrorBodyAddProduct(msg)
    }

    override fun onFailureResponseAddProduct(msg: String) {
        addProductView?.onFailureResponseAddProduct(msg)
    }

}