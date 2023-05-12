package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import com.example.warehouseproject.domain.modelentities.product.Product

interface StockInView {
    fun getResultDataOnRest(data: Product)
    fun showViewOnSuccessResponse()
    fun showViewOnErrorResponse(msg: String)

    fun showViewOnSuccessUpdateQty(data: Product)
    fun showViewOnErrorUpdateQty(msg: String)
}