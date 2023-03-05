package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.data.ProductsPagingSource

class ProductViewModel(private val apiService: ApiService): ViewModel() {
    val listDataProduct = Pager(PagingConfig(pageSize = 6)) {
        ProductsPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)
}