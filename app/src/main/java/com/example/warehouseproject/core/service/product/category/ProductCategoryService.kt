package com.example.warehouseproject.core.service.product.category

import android.util.Log
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.model.product.category.CategoryRequest
import com.example.warehouseproject.core.model.product.category.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductCategoryService(private val token: String) {
    fun createCategory(categoryRequest: CategoryRequest, onResponseSuccessBody: (response: CategoryResponse.SingleResponse) -> Unit, onResponseErrorBody: (msg: String) -> Unit, onFailure: (msg: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .createCategory(categoryRequest)
            .enqueue(object : Callback<CategoryResponse.SingleResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse.SingleResponse>,
                    response: Response<CategoryResponse.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onResponseSuccessBody(it)
                        }
                    } else {
                        response.errorBody()?.string()?.let {
                            onResponseErrorBody(it)
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryResponse.SingleResponse>, t: Throwable) {
                    onFailure(t.message.toString())
                }

            })
    }

    fun getCategories(result: (data: List<Category>) -> Unit) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .getCategories()
            .enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            result(it.data)
                        }
                    } else {
                        Log.d("CATEGORY", response.errorBody()?.string() ?: "Error boy wee")
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    Log.d("CATEGORY", t.message.toString())
                }

            })
    }
}