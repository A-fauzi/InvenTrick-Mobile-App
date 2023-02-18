package com.example.warehouseproject.core.model.user

data class UserResponse(
    val message: String
){
    data class SingleResponse(
        val id: String,
        val username: String,
        val email: String,
        // val roles: []
        val accessToken: String
    )
}
