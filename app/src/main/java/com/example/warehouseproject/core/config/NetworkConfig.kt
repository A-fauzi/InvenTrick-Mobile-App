package com.example.warehouseproject.core.config

import com.example.warehouseproject.core.service.product.ProductService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig(baseUrl: String) {
    val baseUrlInit = baseUrl

    // Set Interceptor
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            })
            .addInterceptor(logging)
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlInit)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun productService(): ProductService = getRetrofit().create(ProductService::class.java)
}