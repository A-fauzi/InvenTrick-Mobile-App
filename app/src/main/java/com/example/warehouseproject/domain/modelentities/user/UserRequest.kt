package com.example.warehouseproject.domain.modelentities.user

data class UserRequest(
    val username: String,
    val password: String
) {
    data class StatusActivity(
        val status_activity: String
    )
}
