package com.example.warehouseproject.core.model.user

data class UserResponse(
    val message: String,
    val count: String,
    val data: List<User>,
){
    data class SingleResponse(
        val message: String,
        val data: User,
    )

    data class SignIn(
        val message: String,
        val id: String,
        val username: String,
        val fullName: String,
        val email: String,
        val accessToken: String,
        val path_storage: String,
        val profile_image: String,
        val division: String,
    )
}
