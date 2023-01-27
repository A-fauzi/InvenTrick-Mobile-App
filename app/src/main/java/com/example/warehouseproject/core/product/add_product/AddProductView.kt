package com.example.warehouseproject.core.product.add_product

interface AddProductView {
    fun showProgress()
    fun hideProgress()
    fun setInputError()
    fun navigateToHome()
    fun getDataProduct()
}