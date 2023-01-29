package com.example.warehouseproject.core.product.add_product

import android.app.Activity
import android.net.Uri
import com.example.warehouseproject.core.constant.Constant
import com.google.firebase.storage.FirebaseStorage

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener {


    fun validateAddProduct(inputFormAddProduct: ModelRequestAddProduct, firebaseStorage: FirebaseStorage, uri: Uri) {
        addProductView?.showProgress()
        addProductInteractor.addProduct(inputFormAddProduct, this)

//        This bug
//        addProductInteractor.uploadImageToStorage(firebaseStorage, uri)
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