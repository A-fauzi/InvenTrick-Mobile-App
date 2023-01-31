package com.example.warehouseproject.core.service.product

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.view.product.ModelProduct
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProductApiService {

    interface OnSuccessRequest {
        fun onSuccessResponse()
        fun onSuccessResponseFailInRequest()
        fun onFailureResponse()
    }

    fun addProductApiService(requestAddProduct: ModelProduct, context: Context, listener: OnSuccessRequest) {
        // request api
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .addProduct(requestAddProduct)
            .enqueue(object : Callback<ModelProduct.ProductSingleResponse> {
                override fun onResponse(
                    call: Call<ModelProduct.ProductSingleResponse>,
                    response: Response<ModelProduct.ProductSingleResponse>
                ) {
                   if (response.isSuccessful) {
                       listener.onSuccessResponse()
                       Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                   } else {
                       listener.onSuccessResponseFailInRequest()

                       // convert json to String
                       val gson = Gson()
                       val mNanu = gson.fromJson(response.errorBody()?.string(), ModelProduct.ProductResponse::class.java)
                       Log.d("ProdActivity", mNanu.message)
                       Toast.makeText(context, mNanu.message, Toast.LENGTH_SHORT).show()
                   }
                }

                override fun onFailure(call: Call<ModelProduct.ProductSingleResponse>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        Toast.makeText(context, "RESPONSE FAILURE: ${t.message}", Toast.LENGTH_SHORT).show()
                        listener.onFailureResponse()
                    }
                }

            })
    }

    fun  getDataProduct(context: Context, resultDataCount: (data: List<ModelProduct>, count: String) -> Unit) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .getProducts()
            .enqueue(object : Callback<ModelProduct.ProductResponse>{
                override fun onResponse(
                    call: Call<ModelProduct.ProductResponse>,
                    response: Response<ModelProduct.ProductResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            resultDataCount(it.data, it.count)
                        }
                    } else {
                        Toast.makeText(context, "Error Response: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ModelProduct.ProductResponse>, t: Throwable) {
                    Log.e("MainActivity", t.message.toString())
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }
}