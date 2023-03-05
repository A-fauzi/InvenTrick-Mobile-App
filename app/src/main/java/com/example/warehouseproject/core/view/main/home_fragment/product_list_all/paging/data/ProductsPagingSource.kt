package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import kotlinx.coroutines.delay
import kotlin.math.max

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

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}