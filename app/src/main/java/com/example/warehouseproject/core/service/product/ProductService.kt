package com.example.warehouseproject.core.service.product

import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.domain.modelentities.product.ProductRequest
import com.example.warehouseproject.domain.modelentities.product.ProductResponses
import com.example.warehouseproject.domain.modelentities.product.StockHistory
import com.example.warehouseproject.domain.modelentities.product.category.Category
import com.example.warehouseproject.domain.modelentities.product.category.CategoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @POST("product/create")
    fun addProduct(@Body productRequest: ProductRequest): Call<ProductResponses.SingleResponse>

    @GET("product/all")
    fun getProducts(@Query("page") page: Int): Call<ProductResponses>

    @GET("product/")
    fun getProductByCode(@Query("code_items") codeItem: String): Call<Product>

    @GET("product-status")
    fun getProductByStatus(
        @Query("status")
        status: String
    ): Call<ProductResponses>

    @PUT("product/{id}")
    fun updateProductQty(@Path("id") id: String, @Body productRequestQty: ProductRequest.RequestQtyOnly): Call<ProductResponses.SingleResponse>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") productId: String): Call<ProductResponses.SingleResponse>

    @POST("stock-history")
    fun stockHistory(@Body stockHistoryRequest: StockHistory.StockHistoryRequest): Call<StockHistory.StockHistorySingleResponse>

    @POST("category/create")
    fun createCategory(@Body categoryRequest: Category): Call<CategoryResponse.SingleResponse>

    @GET("category/all")
    fun getCategories(): Call<CategoryResponse>
}