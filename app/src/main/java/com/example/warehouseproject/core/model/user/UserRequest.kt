package com.example.warehouseproject.core.model.user

data class UserRequest(
    val username: String,
    val password: String
) {
    data class StatusActivity(
        val status_activity: String
    )
}
