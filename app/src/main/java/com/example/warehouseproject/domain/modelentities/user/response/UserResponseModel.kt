package com.example.warehouseproject.domain.modelentities.user.response

import com.example.warehouseproject.domain.modelentities.user.request.UserRequestModel

data class UserResponseModel(
    val message: String,
    val count: String,
    val data: List<UserRequestModel>,
){
    data class SingleResponse(
        val message: String,
        val count: String,
        val data: UserRequestModel
    )
}
