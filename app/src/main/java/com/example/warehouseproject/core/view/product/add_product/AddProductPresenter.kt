package com.example.warehouseproject.core.view.product.add_product

import android.app.Activity
import android.content.Context
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.model.product.ProductRequest

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener, ProductApiService.OnSuccessRequest {


    fun validateAddProduct(inputFormAddProduct: ProductRequest) {
        addProductInteractor.addProduct(inputFormAddProduct, this)
    }

    fun requestApiDataProduct(requestAddProduct: ProductRequest, context: Context) {
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

    override fun onSuccessResponse() {
        addProductView?.navigateToHome()
        addProductView?.hideProgressbar()
        addProductView?.showAnimateSuccessAdd()
    }

    override fun onSuccessResponseFailInRequest() {
        addProductView?.hideProgressbar()
        addProductView?.showButton()
    }

    override fun onFailureResponse() {
        addProductView?.hideProgressbar()
        addProductView?.showButton()
    }

    override fun onSuccessDeleteProduct() {

    }
}