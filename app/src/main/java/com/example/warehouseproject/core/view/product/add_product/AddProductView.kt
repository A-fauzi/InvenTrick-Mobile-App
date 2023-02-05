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

    fun hideButton()

    fun showProgressbar()

    fun showSuccessValidation()

    fun getImageCapture()
    fun getImageFromGallery()
}