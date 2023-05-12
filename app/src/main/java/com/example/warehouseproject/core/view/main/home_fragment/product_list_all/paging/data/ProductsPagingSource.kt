package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService

class ProductsPagingSource(private val apiService: ApiService): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getProducts(currentLoadingPageKey)
            val responseData = mutableListOf<Product>()
            val data = response.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return if (responseData.isEmpty()) {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}