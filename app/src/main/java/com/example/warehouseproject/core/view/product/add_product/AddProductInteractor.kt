package com.example.warehouseproject.core.view.product.add_product

import com.example.warehouseproject.core.model.product.ProductRequest

class AddProductInteractor {

    interface OnAddProductFinishedListener {
        fun onInputErrorCode()
        fun onInputErrorName()
        fun onInputErrorQty()
        fun onInputErrorCategory()
        fun onInputErrorSubCategory()
        fun onInputErrorSpec()
        fun onInputErrorPrice()
        fun onInputErrorLocation()
        fun onInputErrorStatus()
        fun onInputErrorModel()
        fun onInputErrorLot()
        fun onInputErrorExp()

        fun onSuccessValidationInput()
    }

    fun addProduct(input: ProductRequest, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputErrorCode()
            input.name.isEmpty()  -> listener.onInputErrorName()
            input.qty.isEmpty() -> listener.onInputErrorQty()
            input.category.isEmpty()  -> listener.onInputErrorCategory()
            input.sub_category.isEmpty()  -> listener.onInputErrorSubCategory()
            input.specification.isEmpty()  -> listener.onInputErrorSpec()
            input.price?.isEmpty() == true -> listener.onInputErrorPrice()
            input.location.isEmpty()  -> listener.onInputErrorLocation()
            input.status.isEmpty()  -> listener.onInputErrorStatus()
            input.model.isEmpty()  -> listener.onInputErrorModel()
            input.lot.isEmpty()  -> listener.onInputErrorLot()
            input.exp.isEmpty()  -> listener.onInputErrorExp()
            else -> listener.onSuccessValidationInput()
        }
    }
}