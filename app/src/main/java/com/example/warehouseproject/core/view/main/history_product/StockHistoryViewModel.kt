package com.example.warehouseproject.core.view.main.history_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class StockHistoryViewModel(private val apiService: ApiService): ViewModel() {
    val listStockHistory = Pager(PagingConfig(pageSize = 6)){
        StockHistoriesPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)
}