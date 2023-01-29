package com.example.warehouseproject.core.service.product

import com.example.warehouseproject.core.product.add_product.ModelRequestAddProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductService {
    @POST("product/create")
    fun addProduct(@Body productRequest: ModelRequestAddProduct): Call<ModelRequestAddProduct>
}