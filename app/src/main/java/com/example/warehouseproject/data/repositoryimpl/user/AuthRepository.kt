package com.example.warehouseproject.data.repositoryimpl.user

import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    interface OnFinishedRequestUser {
        fun onSuccessBody(response: UserResponseModel.SingleResponse)
        fun onErrorBody(message: String)
        fun onFailure(message: String)
    }
    fun signInUser(userRequest: UserAuthRequestModel, listener: OnFinishedRequestUser) {
        NetworkConfig(Constant.BASE_URL, "")
            .userService()
            .signInUser(userRequest)
            .enqueue(object : Callback<UserResponseModel.SingleResponse> {
                override fun onResponse(
                    call: Call<UserResponseModel.SingleResponse>,
                    response: Response<UserResponseModel.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.onSuccessBody(it)
                        }
                    } else {

                        if (response.code() == 503) {
                            listener.onErrorBody("Server Error")
                        } else {
                            // convert json to String
                            val gson = Gson()
                            val msg = gson.fromJson(response.errorBody()?.string(), UserResponseModel::class.java)
                            listener.onErrorBody(msg.message)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponseModel.SingleResponse>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }
}