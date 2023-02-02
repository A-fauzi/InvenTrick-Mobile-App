package com.example.warehouseproject.core.service.product

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.ProductResponses
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

        fun onSuccessDeleteProduct()
    }

    fun addProductApiService(requestAddProduct: ProductRequest, context: Context, listener: OnSuccessRequest) {
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
                       listener.onSuccessResponse()
                       Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                   } else {
                       listener.onSuccessResponseFailInRequest()

                       // convert json to String
                       val gson = Gson()
                       val mNanu = gson.fromJson(response.errorBody()?.string(), ProductResponses.SingleResponse::class.java)
                       Log.d("ProdActivity", mNanu.message)
                       Toast.makeText(context, mNanu.message, Toast.LENGTH_SHORT).show()
                   }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        Toast.makeText(context, "RESPONSE FAILURE: ${t.message}", Toast.LENGTH_SHORT).show()
                        listener.onFailureResponse()
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

    fun deleteProductApiService(id: String, context: Context, listener: OnSuccessRequest) {
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .deleteProduct(id)
            .enqueue(object : Callback<ProductResponses.SingleResponse>{
                override fun onResponse(
                    call: Call<ProductResponses.SingleResponse>,
                    response: Response<ProductResponses.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Success deleted", Toast.LENGTH_SHORT).show()
                        listener.onSuccessDeleteProduct()
                    } else {
                        Toast.makeText(context, "Failed deleted", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProductResponses.SingleResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed Response", Toast.LENGTH_SHORT).show()
                }

            })
    }
}