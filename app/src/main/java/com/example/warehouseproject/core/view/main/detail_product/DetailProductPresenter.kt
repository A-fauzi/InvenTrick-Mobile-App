package com.example.warehouseproject.core.view.main.detail_product

import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService

class DetailProductPresenter(private val view: DetailProductView? , private val productApiService: ProductApiService): ProductApiService.DeleteProductApiServiceListener {

    fun deleteProductService(productId: String?) {
        if (productId != null) {
            productApiService.deleteProductApiService(productId, this)
        }
    }

    override fun onResponseSuccessBody(msg: String, data: Product?) {
        view?.onResponseSuccessBody(msg, data)
    }

    override fun onResponseErrorBody(msg: String) {
        view?.onResponseErrorBody(msg)
    }

    override fun onFailureDeleteProduct(msg: String) {
        view?.onFailureDeleteProduct(msg)
    }
}