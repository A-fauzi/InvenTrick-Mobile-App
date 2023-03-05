package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService

class ProductViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}