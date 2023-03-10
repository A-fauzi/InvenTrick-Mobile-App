package com.example.warehouseproject.core.service.user

import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("api/auth/signin")
    fun signInUser(@Body userRequest: UserRequest): Call<UserResponse.SingleResponse>

    @PUT("api/user/update/status/{id}")
    fun updateStatusActivityUser(@Path("id") userId: String, @Body userRequestStatus: UserRequest.StatusActivity): Call<UserResponse.SingleResponse>
}