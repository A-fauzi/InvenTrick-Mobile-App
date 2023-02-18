package com.example.warehouseproject.core.service.user

import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @POST("api/auth/signin")
    fun signInUser(@Body userRequest: UserRequest): Call<UserResponse.SingleResponse>
}