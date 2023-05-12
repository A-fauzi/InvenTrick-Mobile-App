package com.example.warehouseproject.core.view.main.history_product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.warehouseproject.domain.modelentities.product.StockHistory

class StockHistoriesPagingSource(private val apiService: ApiService): PagingSource<Int, StockHistory>() {
    override fun getRefreshKey(state: PagingState<Int, StockHistory>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StockHistory> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getStockHistories(currentLoadingPageKey)
            val responseData = mutableListOf<StockHistory>()
            val data = response.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1 ) null else currentLoadingPageKey - 1

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
        }catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}