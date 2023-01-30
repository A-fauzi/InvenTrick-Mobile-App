package com.example.warehouseproject.core.product.add_product

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.warehouseproject.core.helper.PreferenceHelper.saveData
import com.example.warehouseproject.core.product.ModelProduct
import com.example.warehouseproject.core.service.product.ProductApiService
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductInteractor {


    interface OnAddProductFinishedListener {
        fun onInputError()
        fun onSuccess()
    }

    fun addProduct(input: ModelProduct, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputError()
            input.name.isEmpty()  -> listener.onInputError()
            input.qty.toString().isEmpty() -> listener.onInputError()
            input.category.isEmpty()  -> listener.onInputError()
            input.sub_category.isEmpty()  -> listener.onInputError()
            input.specification.isEmpty()  -> listener.onInputError()
            input.price.toString().isEmpty()  -> listener.onInputError()
            input.location.isEmpty()  -> listener.onInputError()
            input.status.isEmpty()  -> listener.onInputError()
            input.model.isEmpty()  -> listener.onInputError()
            input.code_oracle.isEmpty()  -> listener.onInputError()
            input.description_oracle.isEmpty()  -> listener.onInputError()
            else -> listener.onSuccess()
        }
    }

    fun requestApiDataProduct(requestAddProduct: ModelProduct, context: Context) {
        ProductApiService().addProductApiService(requestAddProduct, context)
    }
}