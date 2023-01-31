package com.example.warehouseproject.core.view.product.add_product

import com.example.warehouseproject.core.view.product.ModelProduct

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
        fun onInputErrorCodeOracle()
        fun onInputErrorDescOracle()

        fun onSuccessValidationInput()
    }

    fun addProduct(input: ModelProduct, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputErrorCode()
            input.name.isEmpty()  -> listener.onInputErrorName()
            input.qty.isEmpty() -> listener.onInputErrorQty()
            input.category.isEmpty()  -> listener.onInputErrorCategory()
            input.sub_category.isEmpty()  -> listener.onInputErrorSubCategory()
            input.specification.isEmpty()  -> listener.onInputErrorSpec()
            input.price.isEmpty()  -> listener.onInputErrorPrice()
            input.location.isEmpty()  -> listener.onInputErrorLocation()
            input.status.isEmpty()  -> listener.onInputErrorStatus()
            input.model.isEmpty()  -> listener.onInputErrorModel()
            input.code_oracle.isEmpty()  -> listener.onInputErrorCodeOracle()
            input.description_oracle.isEmpty()  -> listener.onInputErrorDescOracle()
            else -> listener.onSuccessValidationInput()
        }
    }
}