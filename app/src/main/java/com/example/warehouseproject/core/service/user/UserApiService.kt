package com.example.warehouseproject.core.service.user

import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiService {
    interface OnFinishedRequestUser {
        fun onSuccessBody(response: UserResponse.SignIn)
        fun onErrorBody(message: String)
        fun onFailure(message: String)
    }
    fun signInUser(userRequest: UserRequest, listener: OnFinishedRequestUser) {
        NetworkConfig(Constant.BASE_URL, "")
            .userService()
            .signInUser(userRequest)
            .enqueue(object : Callback<UserResponse.SignIn>{
                override fun onResponse(
                    call: Call<UserResponse.SignIn>,
                    response: Response<UserResponse.SignIn>
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

                override fun onFailure(call: Call<UserResponse.SignIn>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }

    interface OnFinishedStatusRequest {
        fun onSuccessBodyReqStatus(response: UserResponse.SingleResponse)
        fun onErrorBodyReqStatus(message: String)
        fun onFailure(message: String)
    }

    fun updateStatusUser(token: String, userId: String, requestStatus: UserRequest.StatusActivity, listener: OnFinishedStatusRequest) {
        NetworkConfig(Constant.BASE_URL, token)
            .userService()
            .updateStatusActivityUser(userId, requestStatus)
            .enqueue(object : Callback<UserResponse.SingleResponse> {
                override fun onResponse(
                    call: Call<UserResponse.SingleResponse>,
                    response: Response<UserResponse.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { listener.onSuccessBodyReqStatus(it) }
                    } else {
                        // convert json to String
                        val gson = Gson()
                        val msg = gson.fromJson(response.errorBody()?.string(), UserResponse::class.java)
                        listener.onErrorBodyReqStatus(msg.message)
                    }
                }

                override fun onFailure(call: Call<UserResponse.SingleResponse>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }
}