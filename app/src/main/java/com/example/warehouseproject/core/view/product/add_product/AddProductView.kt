package com.example.warehouseproject.core.view.product.add_product

import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.model.product.category.Category

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

    fun onDataCategoryRequestNotEmptyView(data: List<Category>)
    fun onDataCategoryRequestIsEmptyView()

    fun searchItemsIsNullView()
    fun searchItemsIsNotNullView(data: ProductModelAssets?)
}