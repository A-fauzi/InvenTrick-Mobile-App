package com.example.warehouseproject.domain.modelentities.product

import android.os.Parcelable
import com.example.warehouseproject.domain.modelentities.user.request.UserRequestModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(

    var _id: String? = null,
    var code_items: String? = null,
    var user: UserRequestModel? = null,
    var name: String? = null,
    var qty: String? = null,
    var category: String? = null,
    var sub_category: String? = null,
    var image: String? = null,
    var specification: String? = null,
    var price: Int? = null,
    var location: String? = null,
    var status: String? = null,
    var model: String? = null,
    var lot: String? = null,
    var exp: String? = null,
    var path_storage: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null

): Parcelable
