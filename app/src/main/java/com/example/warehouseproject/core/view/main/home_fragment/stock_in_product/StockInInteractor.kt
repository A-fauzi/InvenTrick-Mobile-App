package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.content.Context
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.service.product.ProductApiService

class StockInInteractor {
    interface InteractorListener {
        fun onResultData(data: Product)
        fun onResponseSuccess()
        fun onResponseError(msg: String)

        fun onSuccessUpdateQty(data: Product)
    }
    fun productByCode(codeProduct: String, listener: InteractorListener) {
        ProductApiService().getProductByCode(codeProduct, { data ->
            listener.onResultData(data)
        }, {
           listener.onResponseSuccess()
        }, { msg ->
            listener.onResponseError(msg)
        })
    }

    fun productUpdateQty(context: Context, id: String, qtyOnly: ProductRequest.RequestQtyOnly, listener: InteractorListener) {
        ProductApiService().updateProductQty(context, id, qtyOnly) { msg, data ->
            listener.onSuccessUpdateQty(data)
        }
    }
}