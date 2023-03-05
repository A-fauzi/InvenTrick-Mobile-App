package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api

import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.ProductResponses
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("product/all")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): ProductResponses

    companion object {
        fun create(token: String): ApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val newRequest = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .addHeader("x-access-token", token)
                        .build()
                    chain.proceed(newRequest)
                })
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}