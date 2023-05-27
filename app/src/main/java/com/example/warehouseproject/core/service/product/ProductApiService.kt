package com.example.warehouseproject.core.service.product

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.domain.modelentities.product.ProductRequest
import com.example.warehouseproject.domain.modelentities.product.ProductResponses
import com.example.warehouseproject.domain.modelentities.product.StockHistory
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProductApiService(private val token: String) {

    interface OnFinishedAddProduct {
        fun onResponseSuccessBodyAddProduct(msg: String, data: Product?)
        fun onResponseErrorBodyAddProduct(msg: String)
        fun onFailureResponseAddProduct(msg: String)
    }
    fun addProductApiService(requestAddProduct: ProductRequest, listener :OnFinishedAddProduct) {
        // request api
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .addProduct(requestAddProduct)
            .enqueue(object : Callback<ProductResponses.SingleResponse> {
                override fun onResponse(
                    call: Call<ProductResponses.SingleResponse>,
                    response: Response<ProductResponses.SingleResponse>
                ) {
                   if (response.isSuccessful) {
                       response.body()?.let {
                           listener.onResponseSuccessBodyAddProduct(it.message, it.data)
                       }
                   } else {
                       // convert json to String
                       val gson = Gson()
                       val mNanu = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                       listener.onResponseErrorBodyAddProduct(mNanu.message)
                   }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        listener.onFailureResponseAddProduct(t.message.toString())
                    }
                }

            })
    }

    interface OnFinishedGetProducts {
        fun successResponseBodyGetProducts(data: List<Product>, productResponses: ProductResponses)
        fun errorResponseBodyGetProducts(msg: String)
        fun onFailureRequestGetProducts(msg: String)
    }
    fun getDataProduct(page: Int = 1, listener: OnFinishedGetProducts) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .getProducts(page)
            .enqueue(object : Callback<ProductResponses>{
                override fun onResponse(
                    call: Call<ProductResponses>,
                    response: Response<ProductResponses>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.successResponseBodyGetProducts(it.data, it)
                        }
                    } else {
                        // convert json to String
                        val gson = Gson()
                        val msg = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                        listener.errorResponseBodyGetProducts(msg.message)
                    }

                }

                override fun onFailure(call: Call<ProductResponses>, t: Throwable) {
                    listener.onFailureRequestGetProducts(t.message.toString())
                }

            })
    }

    interface DeleteProductApiServiceListener {
        fun onResponseSuccessBody(msg: String, data: Product?)
        fun onResponseErrorBody(msg: String)
        fun onFailureDeleteProduct(msg: String)
    }
    fun deleteProductApiService(productId: String, listener: DeleteProductApiServiceListener) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .deleteProduct(productId)
            .enqueue(object : Callback<ProductResponses.SingleResponse>{
                override fun onResponse(
                    call: Call<ProductResponses.SingleResponse>,
                    response: Response<ProductResponses.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.onResponseSuccessBody(it.message, it.data)
                        }
                    } else {
                        response.body()?.let {
                            listener.onResponseErrorBody(it.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    listener.onFailureDeleteProduct(t.message.toString())
                }

            })
    }

    fun getProductByCode( codeProduct: String, getResultData: (product: Product) -> Unit, onResponseSuccessBody: () -> Unit, onResponseErrorBody: (msg: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .getProductByCode(codeProduct)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                   if (response.isSuccessful) {
                       response.body()?.let {
                           getResultData(it)
                       }
                       onResponseSuccessBody()
                   } else {
                       // convert json to String
                       val gson = Gson()
                       val mNanu = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                       onResponseErrorBody(mNanu.message)
                   }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Log.d("StockInActivity", t.message.toString())
                }

            })
    }

    fun updateProduct(context: Context, id: String, productRequest: ProductRequest, onResponseSuccessBody: (msg: String, data: Product) -> Unit, onResponseErrorBody: (msg: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .updateProduct(id, productRequest)
            .enqueue(object : Callback<ProductResponses.SingleResponse> {
                override fun onResponse(
                    call: Call<ProductResponses.SingleResponse>,
                    response: Response<ProductResponses.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onResponseSuccessBody(it.message, it.data)
                        }
                    } else {
                        // convert json to String
                        val gson = Gson()
                        val mNanu = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                        onResponseErrorBody(mNanu.message)
                    }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    // Create api service stock history
    fun createStockHistory(request: StockHistory.StockHistoryRequest) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .stockHistory(request)
            .enqueue(object : Callback<StockHistory.StockHistorySingleResponse> {
                override fun onResponse(
                    call: Call<StockHistory.StockHistorySingleResponse>,
                    response: Response<StockHistory.StockHistorySingleResponse>
                ) {
                   if (response.isSuccessful) {
                       Log.d("StockHistoryActivity", "SUCCESS MSG: ${response.body()?.message}")
                       Log.d("StockHistoryActivity", "SUCCESS DATA: ${response.body()?.data}")
                   } else {
                       Log.d("StockHistoryActivity", "ERROR RESPONSE: ${response.body()?.message}")
                   }
                }

                override fun onFailure(
                    call: Call<StockHistory.StockHistorySingleResponse>,
                    t: Throwable
                ) {
                    Log.d("StockHistoryActivity", "ON FAILURE: ${t.message}")
                }

            })
    }

    interface OnFinishedGetProductByStatus {
        fun onErrorGetProductByStatus(message: String)
        fun onFailureResponseGetProductByStatus(msg: String)
    }

    fun getProductByStatus(status: String, onResponseSuccessBody: (productResponse: ProductResponses) -> Unit, listener: OnFinishedGetProductByStatus) {
        NetworkConfig(Constant.BASE_URL, token)
            .productService()
            .getProductByStatus(status)
            .enqueue(object : Callback<ProductResponses>{
                override fun onResponse(call: Call<ProductResponses>, response: Response<ProductResponses>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onResponseSuccessBody(it)
                        }
                    } else {
                        // convert json to String
                        val gson = Gson()
                        val msg = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                        listener.onErrorGetProductByStatus(msg.message)
                    }
                }

                override fun onFailure(call: Call<ProductResponses>, t: Throwable) {
                    listener.onFailureResponseGetProductByStatus(t.message.toString())
                }

            })
    }
}