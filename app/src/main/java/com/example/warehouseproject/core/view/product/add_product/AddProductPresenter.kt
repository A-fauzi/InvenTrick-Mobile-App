package com.example.warehouseproject.core.view.product.add_product

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.product.ModelProduct
import com.google.firebase.storage.FirebaseStorage

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener, ProductApiService.OnSuccessRequest {


    fun validateAddProduct(inputFormAddProduct: ModelProduct) {
        addProductInteractor.addProduct(inputFormAddProduct, this)
    }

    fun requestApiDataProduct(requestAddProduct: ModelProduct, context: Context) {
        ProductApiService().addProductApiService(requestAddProduct, context, this)
    }

    fun resultImageFromGallery(requestCode: Int, resultCode: Int, getFile: () -> Unit?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.REQUEST_CODE) {
            getFile()
        }
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

    override fun onInputErrorCodeOracle() {
        addProductView?.showInputErrorCodeOracle()
    }

    override fun onInputErrorDescOracle() {
        addProductView?.showInputErrorDescOracle()
    }

    override fun onSuccessValidationInput() {
        addProductView?.showSuccessValidation()
        addProductView?.hideButton()
        addProductView?.showProgressbar()
    }

    override fun onSuccessRequest() {
        addProductView?.navigateToHome()
        addProductView?.hideProgressbar()
    }

    override fun onFailureRequest() {
        addProductView?.hideProgressbar()
        addProductView?.hideButton()
    }
}