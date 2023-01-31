package com.example.warehouseproject.core.view.product.add_product

import android.net.Uri
import com.example.warehouseproject.core.view.product.ModelProduct

interface AddProductView {
    fun showInputErrorCode()
    fun showInputErrorName()
    fun showInputErrorQty()
    fun showInputErrorCategory()
    fun showInputErrorSubCategory()
    fun showInputErrorSpec()
    fun showInputErrorPrice()
    fun showInputErrorLocation()
    fun showInputErrorStatus()
    fun showInputErrorModel()
    fun showInputErrorCodeOracle()
    fun showInputErrorDescOracle()

    fun showButton()
    fun hideButton()

    fun showProgressbar()
    fun hideProgressbar()

    fun showSuccessValidation()

    fun getImageCapture()
    fun getImageFromGallery()

    fun navigateToHome()
}