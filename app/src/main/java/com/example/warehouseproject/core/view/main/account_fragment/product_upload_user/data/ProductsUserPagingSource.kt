package com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
import io.paperdb.Paper
import okio.IOException
import retrofit2.HttpException

class ProductsUserPagingSource(private val productsUserApiService: ProductsUserApiService): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val uid = Paper.book().read<String>("id").toString()
            val currentLoadingPageKey = params.key ?: 1
            val response = productsUserApiService.getProductsUser(uid, currentLoadingPageKey)
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
        }catch (e: IOException){
            // IOException for network failures.
            return LoadResult.Error(e)
        }catch (e: HttpException){
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}