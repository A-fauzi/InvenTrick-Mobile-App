package com.example.warehouseproject.core.service.product

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.view.product.ModelProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProductApiService {

    interface OnSuccessRequest {
        fun onSuccessRequest()
        fun onFailureRequest()
    }

    fun addProductApiService(requestAddProduct: ModelProduct, context: Context, listener: OnSuccessRequest) {
        // request api
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .addProduct(requestAddProduct)
            .enqueue(object : Callback<ModelProduct> {
                override fun onResponse(
                    call: Call<ModelProduct>,
                    response: Response<ModelProduct>
                ) {
                    Toast.makeText(context, "REQUEST SUCCESS: ${response.message()}", Toast.LENGTH_SHORT).show()
                    listener.onSuccessRequest()
                }

                override fun onFailure(call: Call<ModelProduct>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        Toast.makeText(context, "REQUEST FAILURE: ${t.message}", Toast.LENGTH_SHORT).show()
                        listener.onFailureRequest()
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