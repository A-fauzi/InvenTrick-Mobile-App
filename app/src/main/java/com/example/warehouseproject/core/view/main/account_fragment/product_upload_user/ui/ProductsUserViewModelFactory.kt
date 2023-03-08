package com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
import java.lang.IllegalArgumentException

class ProductsUserViewModelFactory(private val productsUserApiService: ProductsUserApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsUserViewModel::class.java)) {
            return ProductsUserViewModel(productsUserApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}