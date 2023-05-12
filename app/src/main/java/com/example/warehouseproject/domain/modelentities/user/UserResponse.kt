package com.example.warehouseproject.domain.modelentities.user

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
