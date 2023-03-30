package com.example.warehouseproject.core.view.product.add_product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class AddProductPresenter(var addProductView: AddProductView?, private val productApiService: ProductApiService): ProductApiService.OnFinishedAddProduct {

    fun addProductRequest(request: ProductRequest) {
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