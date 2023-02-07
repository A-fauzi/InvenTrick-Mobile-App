package com.example.warehouseproject.core.service.product

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.model.product.StockHistory
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProductApiService {

    fun addProductApiService(requestAddProduct: ProductRequest, onResponseSuccessBody: (msg: String, data: Product? ) -> Unit, onResponseErrorBody: (msg: String) -> Unit, onFailure: (msg: String) -> Unit) {
        // request api
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .addProduct(requestAddProduct)
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
                    if(t is SocketTimeoutException) {
                        onFailure(t.message.toString())
                    }
                }

            })
    }

    fun getDataProduct(context: Context, resultDataCount: (data: List<Product>, count: String) -> Unit, viewVisibilitySuccess: () -> Unit) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .getProducts()
            .enqueue(object : Callback<ProductResponses>{
                override fun onResponse(
                    call: Call<ProductResponses>,
                    response: Response<ProductResponses>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            resultDataCount(it.data, it.count, )
                        }
                        viewVisibilitySuccess()
                    } else {
                        Toast.makeText(context, "Error Response: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ProductResponses>, t: Throwable) {
                    Log.e("MainActivity", t.message.toString())
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun deleteProductApiService(id: String, onResponseSuccessBody: (msg: String, data: Product? ) -> Unit, onResponseErrorBody: (msg: String) -> Unit, onFailure: (msg: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .deleteProduct(id)
            .enqueue(object : Callback<ProductResponses.SingleResponse>{
                override fun onResponse(
                    call: Call<ProductResponses.SingleResponse>,
                    response: Response<ProductResponses.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onResponseSuccessBody(it.message, it.data)
                        }
                    } else {
                        response.body()?.let {
                            onResponseErrorBody(it.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    onFailure(t.message.toString())
                }

            })
    }

    fun getProductByCode(context: Context, codeProduct: String, getResultData: (product: Product) -> Unit, onResponseSuccessBody: () -> Unit, onResponseErrorBody: (msg: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL)
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

    fun updateProductQty(context: Context, id: String, qtyOnly: ProductRequest.RequestQtyOnly, onResponseSuccessBody: (msg: String, data: Product) -> Unit) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .updateProductQty(id, qtyOnly)
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
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    // Create api service stock history
    fun createStockHistory(request: StockHistory.StockHistoryRequest) {
        NetworkConfig(Constant.BASE_URL)
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

    fun getStockHistories(onResponseSuccessBody: (msg: String, data: List<StockHistory>, count: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .getStockHistories()
            .enqueue(object : Callback<StockHistory.StockHistoryAllResponse> {
                override fun onResponse(
                    call: Call<StockHistory.StockHistoryAllResponse>,
                    response: Response<StockHistory.StockHistoryAllResponse>
                ) {
                    if (response.isSuccessful) {
                       response.body()?.let {
                           onResponseSuccessBody(it.message, it.data, it.count)
                       }
                    } else {
                        Log.d("Histories", response.message())
                    }
                }

                override fun onFailure(
                    call: Call<StockHistory.StockHistoryAllResponse>,
                    t: Throwable
                ) {
                    Log.d("Histories", t.message.toString())
                }

            })
    }

}