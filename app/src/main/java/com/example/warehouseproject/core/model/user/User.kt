package com.example.warehouseproject.core.model.user

data class User(
    val _id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val roles: List<Roles>,
    val division: String,
    val status_activity: String,
    val jwt_token: String,
) {
    data class Roles(
        val _id: String,
        val name: String
    )
}
