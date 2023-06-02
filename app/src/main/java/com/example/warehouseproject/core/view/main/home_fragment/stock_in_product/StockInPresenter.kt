package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.content.Context
import com.example.warehouseproject.domain.modelentities.product.Product

class StockInPresenter(private val view: StockInView, private val interactor: StockInInteractor): StockInInteractor.InteractorListener {
    fun searchProduct(context: Context, codeProduct: String) {
        interactor.productByCode(context, codeProduct, this)
    }

    fun updateProduct(context: Context, id: String, productRequest: Product) {
        interactor.productUpdate(context, id, productRequest, this)
    }

    override fun onResultData(data: Product) {
        view.getResultDataOnRest(data)
    }

    override fun onResponseSuccess() {
        view.showViewOnSuccessResponse()
    }

    override fun onResponseError(msg: String) {
        view.showViewOnErrorResponse(msg)
    }

    override fun onSuccessUpdateQty(data: Product) {
        view.showViewOnSuccessUpdateQty(data)
    }

    override fun onErrorUpdateQty(msg: String) {
        view.showViewOnErrorUpdateQty(msg)
    }
}