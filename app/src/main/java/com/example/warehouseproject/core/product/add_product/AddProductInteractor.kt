package com.example.warehouseproject.core.product.add_product

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.core.config.NetworkConfig
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.helper.PreferenceHelper.saveData
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*

class AddProductInteractor {

    private lateinit var product: ModelRequestAddProduct


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

    fun requestApiDataProduct(requestAddProduct: ModelRequestAddProduct, context: Context) {
        // request api
        NetworkConfig(Constant.BASE_URL)
            .productService()
            .addProduct(requestAddProduct)
            .enqueue(object : Callback<ModelRequestAddProduct>{
                override fun onResponse(
                    call: Call<ModelRequestAddProduct>,
                    response: Response<ModelRequestAddProduct>
                ) {
                    Toast.makeText(context, "request success: ${response.message()}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ModelRequestAddProduct>, t: Throwable) {
                    if(t is SocketTimeoutException) {
                        Toast.makeText(context, "request failure: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }
}