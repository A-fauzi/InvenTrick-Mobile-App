package com.example.warehouseproject.core.model.user

data class User(
    val _id: String,
    val username: String,
    val profile_image: String,
    val fullName: String,
    val email: String,
    val roles: List<Roles>,
    val division: String,
    val status_activity: String,
    val jwt_token: String,
    val path_storage: String
) {
    data class Roles(
        val _id: String,
        val name: String
    )
}
