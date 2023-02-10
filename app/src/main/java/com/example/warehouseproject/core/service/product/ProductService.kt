package com.example.warehouseproject.core.service.product

import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.model.product.category.CategoryRequest
import com.example.warehouseproject.core.model.product.category.CategoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @POST("product/create")
    fun addProduct(@Body productRequest: ProductRequest): Call<ProductResponses.SingleResponse>

    @GET("product/all")
    fun getProducts(): Call<ProductResponses>

    @GET("product/")
    fun getProductByCode(@Query("code_items") codeItem: String): Call<Product>

    @PUT("product/{id}")
    fun updateProductQty(@Path("id") id: String, @Body productRequestQty: ProductRequest.RequestQtyOnly): Call<ProductResponses.SingleResponse>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") productId: String): Call<ProductResponses.SingleResponse>

    @POST("stock-history")
    fun stockHistory(@Body stockHistoryRequest: StockHistory.StockHistoryRequest): Call<StockHistory.StockHistorySingleResponse>

    @GET("stock-history")
    fun getStockHistories(): Call<StockHistory.StockHistoryAllResponse>

    @POST("category/create")
    fun createCategory(@Body categoryRequest: CategoryRequest): Call<CategoryResponse.SingleResponse>

    @GET("category/all")
    fun getCategories(): Call<CategoryResponse>
}