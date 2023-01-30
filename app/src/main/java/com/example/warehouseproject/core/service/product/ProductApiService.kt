package com.example.warehouseproject.core.service.product

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.main.MainActivity
import com.example.warehouseproject.core.product.ModelProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProductApiService {

    fun addProductApiService(requestAddProduct: ModelProduct, context: Context) {
        // request api
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .addProduct(requestAddProduct)
            .enqueue(object : Callback<ModelProduct> {
                override fun onResponse(
                    call: Call<ModelProduct>,
                    response: Response<ModelProduct>
                ) {
                    Toast.makeText(context, "request success: ${response.message()}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ModelProduct>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        Toast.makeText(context, "request failure: ${t.message}", Toast.LENGTH_SHORT).show()
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