package com.example.warehouseproject.core.product.add_product

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.product.ModelProduct
import com.google.firebase.storage.FirebaseStorage

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener {


    fun validateAddProduct(inputFormAddProduct: ModelProduct) {
        addProductView?.showProgress()
        addProductInteractor.addProduct(inputFormAddProduct, this)
    }


    fun uploadImageToFirebase(firebaseStorage: FirebaseStorage, uri: Uri, context: Context) {
//        addProductInteractor.uploadImageToFirebaseStorage( firebaseStorage, uri, context)
    }

    fun requestApiDataProduct(requestAddProduct: ModelProduct, context: Context) {
        addProductInteractor.requestApiDataProduct(requestAddProduct, context)
    }

    fun resultImageFromGallery(requestCode: Int, resultCode: Int, getFile: () -> Unit?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.REQUEST_CODE) {
            getFile()
        }
    }

    override fun onInputError() {
        addProductView?.apply {
            setInputError()
            hideProgress()
        }
    }

    override fun onSuccess() {
        addProductView?.navigateToHome()
        addProductView?.storeToDatabase()
    }
}