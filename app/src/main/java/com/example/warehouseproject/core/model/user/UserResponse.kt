package com.example.warehouseproject.core.model.user

data class UserResponse(
    val message: String,
    val count: String,
    val data: List<User>,
){
    data class SingleResponse(
        val message: String,
        val count: String,
        val data: User
    )
}
