package com.example.warehouseproject.core.view.product.add_product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.squareup.picasso.Picasso

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener {


    fun validateAddProduct(inputFormAddProduct: ProductRequest) {
        addProductInteractor.addProduct(inputFormAddProduct, this)
    }

    fun requestApiDataProduct(requestAddProduct: ProductRequest, onResponseSuccessBody: (msg: String, data: Product?) -> Unit, onResponseErrorBody: (msg: String) -> Unit, onFailure: (msg: String) -> Unit) {
        ProductApiService().addProductApiService(requestAddProduct, onResponseSuccessBody, onResponseErrorBody, onFailure)
    }

    fun resultImageFromGallery(requestCode: Int, resultCode: Int, data: Intent?) {
        addProductInteractor.resultImageFromGallery(requestCode, resultCode, data, this)
    }

    fun getCategory() {
        addProductInteractor.requestCategoryApi(this)
    }

    fun searchItemsProductName(context: Context, itemCode: String) {
        addProductInteractor.searchItemProductNameByCode(context, "product.json", itemCode, this)
    }

    override fun onInputErrorCode() {
        addProductView?.showInputErrorCode()
    }

    override fun onInputErrorName() {
        addProductView?.showInputErrorName()
    }

    override fun onInputErrorQty() {
        addProductView?.showInputErrorQty()
    }

    override fun onInputErrorCategory() {
        addProductView?.showInputErrorCategory()
    }

    override fun onInputErrorSubCategory() {
        addProductView?.showInputErrorSubCategory()
    }

    override fun onInputErrorSpec() {
        addProductView?.showInputErrorSpec()
    }

    override fun onInputErrorPrice() {
        addProductView?.showInputErrorPrice()
    }

    override fun onInputErrorLocation() {
        addProductView?.showInputErrorLocation()
    }

    override fun onInputErrorStatus() {
        addProductView?.showInputErrorStatus()
    }

    override fun onInputErrorModel() {
        addProductView?.showInputErrorModel()
    }

    override fun onInputErrorLot() {
        addProductView?.showInputErrorLot()
    }

    override fun onInputErrorExp() {
        addProductView?.showInputErrorExp()
    }

    override fun onSuccessValidationInput() {
        addProductView?.showSuccessValidation()
        addProductView?.hideButton()
        addProductView?.showProgressbar()
    }

    override fun onDataCategoryRequestNotEmpty(data: List<Category>) {
        addProductView?.onDataCategoryRequestNotEmptyView(data)
    }

    override fun onDataCategoryRequestIsEmpty() {
        addProductView?.onDataCategoryRequestIsEmptyView()
    }

    override fun searchItemsIsNull() {
        addProductView?.searchItemsIsNullView()
    }

    override fun searchItemsIsNotNull(data: ProductModelAssets?) {
        addProductView?.searchItemsIsNotNullView(data)
    }

    override fun onSuccessTryResultImageFromGallery(data: Intent?) {
        addProductView?.onSuccessTryResultImageFromGalleryView(data)
    }

    override fun onFailureCatchResultImageFromGallery(e: Exception) {
        addProductView?.onFailureCatchResultImageFromGalleryView(e)
    }

}