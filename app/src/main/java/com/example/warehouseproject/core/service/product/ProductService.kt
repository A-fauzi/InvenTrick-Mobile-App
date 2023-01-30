package com.example.warehouseproject.core.service.product

import com.example.warehouseproject.core.product.ModelProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @POST("product/create")
    fun addProduct(@Body productRequest: ModelProduct): Call<ModelProduct>

    @GET("product/all")
    fun getProducts(): Call<ModelProduct.ProductResponse>
}