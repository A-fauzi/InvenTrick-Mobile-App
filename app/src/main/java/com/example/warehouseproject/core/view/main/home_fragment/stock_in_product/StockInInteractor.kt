package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.content.Context
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.service.product.ProductApiService
import io.paperdb.Paper

class StockInInteractor {
    interface InteractorListener {
        fun onResultData(data: Product)
        fun onResponseSuccess()
        fun onResponseError(msg: String)

        fun onSuccessUpdateQty(data: Product)
        fun onErrorUpdateQty(msg: String)
    }
    fun productByCode(context: Context, codeProduct: String, listener: InteractorListener) {
        Paper.init(context)
        val token = Paper.book().read<String>("token").toString()
        ProductApiService(token).getProductByCode(codeProduct, { data ->
            listener.onResultData(data)
        }, {
           listener.onResponseSuccess()
        }, { msg ->
            listener.onResponseError(msg)
        })
    }

    fun productUpdateQty(context: Context, id: String, qtyOnly: ProductRequest.RequestQtyOnly, listener: InteractorListener) {
        Paper.init(context)
        val token = Paper.book().read<String>("token").toString()
        ProductApiService(token).updateProductQty(context, id, qtyOnly, { msg, data ->
            listener.onSuccessUpdateQty(data)
        }, {
            listener.onErrorUpdateQty(it)
        })
    }
}