package com.example.warehouseproject.core.utils

import android.os.Bundle
import com.example.warehouseproject.domain.modelentities.product.Product

object DataBundle {
    fun putProductData(data: Product): Bundle {
        val bundle = Bundle()
        bundle.putString("id", data._id)
        bundle.putString("image", data.image)
        bundle.putString("name", data.name)
        bundle.putString("code", data.code_items)
        bundle.putString("spec", data.specification)
        bundle.putString("exp", data.exp)
        bundle.putString("qty", data.qty)
        bundle.putString("price", data.price.toString())
        bundle.putString("location", data.location)
        bundle.putString("model", data.model)
        bundle.putString("lot", data.lot)
        bundle.putString("createAt", data.created_at)
        bundle.putString("updateAt", data.updated_at)
        bundle.putString("category", data.category)
        bundle.putString("subcat", data.sub_category)
        bundle.putString("status", data.status)
        bundle.putString("uid", data.user?._id)
        bundle.putString("path", data.path_storage)

        return bundle
    }
}