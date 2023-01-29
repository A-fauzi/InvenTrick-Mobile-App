package com.example.warehouseproject.core.product.add_product

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductInteractor {
    interface OnAddProductFinishedListener {
        fun onInputError()
        fun onSuccess()
    }

    fun addProduct(input: ModelRequestAddProduct, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputError()
            input.name.isEmpty()  -> listener.onInputError()
            input.category.isEmpty()  -> listener.onInputError()
            input.sub_category.isEmpty()  -> listener.onInputError()
            input.image.isEmpty()  -> listener.onInputError()
            input.specification.isEmpty()  -> listener.onInputError()
            input.price.isEmpty()  -> listener.onInputError()
            input.location.isEmpty()  -> listener.onInputError()
            input.status.isEmpty()  -> listener.onInputError()
            input.model.isEmpty()  -> listener.onInputError()
            input.code_oracle.isEmpty()  -> listener.onInputError()
            input.description_oracle.isEmpty()  -> listener.onInputError()
            else -> listener.onSuccess()
        }
    }

    fun uploadImageToStorage(firebaseStorage: FirebaseStorage, uriPath: Uri) {
        val fillName = "profile_${UUID.randomUUID()}.jpg"
        val refStorage =
            firebaseStorage.reference.child("/image_product/$fillName")
        refStorage.putFile(uriPath).addOnSuccessListener { uploadTask ->
            Log.d("MainActivity", "URI Upload Task: $uploadTask")
            uploadTask.storage.downloadUrl.addOnCompleteListener { uri ->
                Log.d("MainActivity", "URI: $uri")
            }.addOnFailureListener {
                Log.d("MainActivity", "URI Failure: ${it.message}")
            }
        }.addOnFailureListener {
            Log.d("MainActivity", "URI Failure Task: ${it.message}")
        }
    }
}