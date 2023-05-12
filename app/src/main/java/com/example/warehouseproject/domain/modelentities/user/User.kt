package com.example.warehouseproject.domain.modelentities.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val _id: String,
    val username: String,
    val profile_image: String,
    val fullName: String,
    val email: String,
    val roles: List<Roles>,
    val position: String,
    val status_activity: String,
    val jwt_token: String,
    val path_storage: String
): Parcelable {
    @Parcelize
    data class Roles(
        val _id: String,
        val name: String
    ): Parcelable
}
