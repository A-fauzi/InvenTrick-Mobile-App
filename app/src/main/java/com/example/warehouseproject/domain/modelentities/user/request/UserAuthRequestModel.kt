package com.example.warehouseproject.domain.modelentities.user.request

data class UserAuthRequestModel(
    val username: String,
    val password: String
) {
    data class StatusActivity(
        val status_activity: String
    )
}
