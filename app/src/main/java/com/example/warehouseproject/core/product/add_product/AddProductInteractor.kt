package com.example.warehouseproject.core.product.add_product

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.warehouseproject.core.helper.PreferenceHelper.saveData
import com.example.warehouseproject.core.service.product.ProductApiService
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductInteractor {

    private lateinit var product: ModelProduct


    interface OnAddProductFinishedListener {
        fun onInputError()
        fun onSuccess()
    }

    fun addProduct(input: ModelProduct, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputError()
            input.name.isEmpty()  -> listener.onInputError()
            input.qty.toString().isEmpty() -> listener.onInputError()
            input.category.isEmpty()  -> listener.onInputError()
            input.sub_category.isEmpty()  -> listener.onInputError()
            input.image.isEmpty()  -> listener.onInputError()
            input.specification.isEmpty()  -> listener.onInputError()
            input.price.toString().isEmpty()  -> listener.onInputError()
            input.location.isEmpty()  -> listener.onInputError()
            input.status.isEmpty()  -> listener.onInputError()
            input.model.isEmpty()  -> listener.onInputError()
            input.code_oracle.isEmpty()  -> listener.onInputError()
            input.description_oracle.isEmpty()  -> listener.onInputError()
            else -> listener.onSuccess()
        }
    }

    fun uploadImageToFirebaseStorage(firebaseStorage: FirebaseStorage, uriPath: Uri, context: Context) {
        val fillName = "product_${UUID.randomUUID()}.jpg"
        val refStorage =
            firebaseStorage.reference.child("/image_product/$fillName")
        refStorage.putFile(uriPath).addOnSuccessListener { uploadTask ->
            // Upload ketika berhasil
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

//                // Save Preference data
//                // save image uri in preference
                saveData(context, uri.toString())


            }.addOnFailureListener {
                Log.e("MainActivity", "URI Failure: ${it.message}")
            }
        }.addOnFailureListener {
            Log.e("MainActivity", "URI Failure Task: ${it.message}")
        }
    }

    fun requestApiDataProduct(requestAddProduct: ModelProduct, context: Context) {
        ProductApiService().addProductApiService(requestAddProduct, context)
    }
}