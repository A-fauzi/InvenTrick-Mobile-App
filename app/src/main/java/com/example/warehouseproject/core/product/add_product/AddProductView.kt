package com.example.warehouseproject.core.product.add_product

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

interface AddProductView {
    fun showProgress()
    fun hideProgress()
    fun setInputError()
    fun navigateToHome()

    fun storeToDatabase()
    fun getImageCapture()
    fun getImageFromGallery()
}