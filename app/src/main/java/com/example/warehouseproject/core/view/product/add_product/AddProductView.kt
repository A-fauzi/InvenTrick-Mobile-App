package com.example.warehouseproject.core.view.product.add_product

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
    fun showInputErrorLot()
    fun showInputErrorExp()

    fun showButton()
    fun hideButton()

    fun showProgressbar()
    fun hideProgressbar()

    fun showAnimateSuccessAdd()

    fun showSuccessValidation()

    fun getImageCapture()
    fun getImageFromGallery()

    fun navigateToHome()
}