package com.example.warehouseproject.core.view.main.history_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StockHistoryViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockHistoryViewModel::class.java)) {
            return StockHistoryViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}