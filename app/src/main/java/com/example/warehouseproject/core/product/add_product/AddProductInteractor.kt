package com.example.warehouseproject.core.product.add_product

class AddProductInteractor {
    interface OnAddProductFinishedListener {
        fun onInputError()
        fun onSuccess()
    }

    fun addProduct(input: ModelRequestAddProduct, listener: OnAddProductFinishedListener) {
        when {
            input.code.text.isEmpty() -> listener.onInputError()
            input.name.text.isEmpty() -> listener.onInputError()
            input.category.text.isEmpty() -> listener.onInputError()
            input.subCategory.text.isEmpty() -> listener.onInputError()
//            input.image.text.isEmpty() -> listener.onInputError()
            input.spec.text.isEmpty() -> listener.onInputError()
            input.price.text.isEmpty() -> listener.onInputError()
            input.location.text.isEmpty() -> listener.onInputError()
            input.status.text.isEmpty() -> listener.onInputError()
            input.model.text.isEmpty() -> listener.onInputError()
            input.codeOracle.text.isEmpty() -> listener.onInputError()
            input.descOracle.text.isEmpty() -> listener.onInputError()
            else -> listener.onSuccess()
        }
    }
}