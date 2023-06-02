package com.example.warehouseproject.core.service.user

import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.domain.modelentities.user.request.UserRequestModel
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiService {
    interface OnFinishedRequestUser {
        fun onSuccessBody(response: UserResponseModel.SingleResponse)
        fun onErrorBody(message: String)
        fun onFailure(message: String)
    }
    fun signInUser(userRequest: UserAuthRequestModel, listener: OnFinishedRequestUser) {
        NetworkConfig(Constant.BASE_URL, "")
            .userService()
            .signInUser(userRequest)
            .enqueue(object : Callback<UserResponseModel.SingleResponse>{
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
                            val mNanu = gson.fromJson(response.errorBody()?.string(), UserResponseModel::class.java)
                            listener.onErrorBody(mNanu.message)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponseModel.SingleResponse>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }

    interface OnFinishedStatusRequest {
        fun onSuccessBodyReqStatus(response: UserResponseModel.SingleResponse)
        fun onErrorBodyReqStatus(message: String)
        fun onFailure(message: String)
    }

    fun updateStatusUser(token: String, userId: String, requestStatus: UserAuthRequestModel.StatusActivity, listener: OnFinishedStatusRequest) {
        NetworkConfig(Constant.BASE_URL, token)
            .userService()
            .updateStatusActivityUser(userId, requestStatus)
            .enqueue(object : Callback<UserResponseModel.SingleResponse> {
                override fun onResponse(
                    call: Call<UserResponseModel.SingleResponse>,
                    response: Response<UserResponseModel.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { listener.onSuccessBodyReqStatus(it) }
                    } else {
                        if (response.code() == 503) {
                            listener.onErrorBodyReqStatus("Server Error")
                        } else {
                            // convert json to String
                            val gson = Gson()
                            val mNanu = gson.fromJson(response.errorBody()?.string(), UserResponseModel::class.java)
                            listener.onErrorBodyReqStatus(mNanu.message)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponseModel.SingleResponse>, t: Throwable) {
                    listener.onFailure(t.message.toString())
                }

            })
    }

    fun getUserById(token: String, userId: String, onSuccessBody: (user: UserRequestModel?) -> Unit, onErrorBody: (msg: String) -> Unit, onFailure: (msg: String) -> Unit){
        NetworkConfig(Constant.BASE_URL, token)
            .userService()
            .getUserById(userId)
            .enqueue(object : Callback<UserResponseModel.SingleResponse>{
                override fun onResponse(
                    call: Call<UserResponseModel.SingleResponse>,
                    response: Response<UserResponseModel.SingleResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data.let {
                            onSuccessBody(it)
                        }
                    } else {
                        if (response.code() == 503) {
                            onErrorBody("Server Error")
                        } else {
                            // convert json to String
                            val gson = Gson()
                            val mNanu = gson.fromJson(response.errorBody()?.string(), UserResponseModel::class.java)
                            onErrorBody(mNanu.message)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponseModel.SingleResponse>, t: Throwable) {
                    onFailure(t.message.toString())
                }

            })
    }
}