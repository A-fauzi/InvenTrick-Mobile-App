package com.example.warehouseproject.core.service.product

import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.ProductResponses
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @POST("product/create")
    fun addProduct(@Body productRequest: ProductRequest): Call<ProductResponses.SingleResponse>

    @GET("product/all")
    fun getProducts(): Call<ProductResponses>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") productId: String): Call<ProductResponses.SingleResponse>
}