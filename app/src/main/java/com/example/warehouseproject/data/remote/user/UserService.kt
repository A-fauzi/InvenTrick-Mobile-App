package com.example.warehouseproject.data.remote.user

import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("api/auth/signin")
    fun signInUser(@Body userRequest: UserAuthRequestModel): Call<UserResponseModel.SingleResponse>

    @PUT("api/user/update/status/{id}")
    fun updateStatusActivityUser(@Path("id") userId: String, @Body userRequestStatus: UserAuthRequestModel.StatusActivity): Call<UserResponseModel.SingleResponse>

    @GET("api/user/{id}")
    fun getUserById(@Path("id") userId: String): Call<UserResponseModel.SingleResponse>
}