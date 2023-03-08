package com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.data.ProductsUserPagingSource

class ProductsUserViewModel(private val productsUserApiService: ProductsUserApiService): ViewModel() {
    val listDataProductsUser = Pager(PagingConfig(pageSize = 10)) {
        ProductsUserPagingSource(productsUserApiService)
    }.flow.cachedIn(viewModelScope)
}