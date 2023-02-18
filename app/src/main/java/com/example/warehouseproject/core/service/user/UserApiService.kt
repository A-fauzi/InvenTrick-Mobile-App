package com.example.warehouseproject.core.service.user

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiService {
    interface OnFinishedRequestUser {
        fun onSuccessBody(response: UserResponse.SingleResponse)
        fun onErrorBody(message: String)
        fun onFailure(message: String)
    }
    fun signInUser(userRequest: UserRequest, listener: OnFinishedRequestUser) {
        NetworkConfig(Constant.BASE_URL, "")
            .userService()
            .signInUser(userRequest)
            .enqueue(object : Callback<UserResponse.SingleResponse>{
                override fun onResponse(
                    call: Call<UserResponse.SingleResponse>,
                    response: Response<UserResponse.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.onSuccessBody(it)
                        }
                    } else {
                        // convert json to String
                        val gson = Gson()
                        val mNanu = gson.fromJson(response.errorBody()?.string(), UserResponse::class.java)
                        listener.onErrorBody(mNanu.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse.SingleResponse>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }
}