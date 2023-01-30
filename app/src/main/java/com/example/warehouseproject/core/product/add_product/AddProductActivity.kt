package com.example.warehouseproject.core.product.add_product

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.warehouseproject.core.main.MainActivity
import com.example.warehouseproject.core.constant.Constant.REQUEST_CODE
import com.example.warehouseproject.core.helper.PreferenceHelper
import com.example.warehouseproject.core.product.ModelProduct
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductActivity : AppCompatActivity(), AddProductView {
    private lateinit var modelRequestAddProduct: ModelProduct

//    View
    private lateinit var code: EditText
    private lateinit var name: EditText
    private lateinit var category: EditText
    private lateinit var subCategory: EditText
    private lateinit var spec: EditText
    private lateinit var price: EditText
    private lateinit var location: EditText
    private lateinit var status: EditText
    private lateinit var model: EditText
    private lateinit var codeOracle: EditText
    private lateinit var descOracle: EditText

    private lateinit var fillPath: Uri

    private lateinit var firebaseStorage: FirebaseStorage

    private val presenter = AddProductPresenter(this, AddProductInteractor())

//    private var imageCapture: ImageCapture

    private lateinit var binding: ActivityAddProductBinding

    private fun initView() {
       code = binding.etCodeProduct
       name =  binding.etNameProduct
       category = binding.etCategoryProduct
       subCategory = binding.etSubCategoryProduct
       spec =  binding.etSpecProduct
       price = binding.etPriceProduct
       location =  binding.etLocationProduct
       status = binding.etStatusProduct
       model = binding.etModelProduct
       codeOracle = binding.etCodeOracleProduct
       descOracle = binding.etDescOracleProduct
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()



        firebaseStorage = FirebaseStorage.getInstance()

        binding.submitButtonAddProduct.setOnClickListener {
            modelRequestAddProduct = ModelProduct(
                "Kosong",
                "${code.text}",
                "${name.text}",
                "20",
                "${category.text}",
                "${subCategory.text}",
                "${spec.text}",
                price.text.toString(),
                "${location.text}",
                "${status.text}",
                "${model.text}",
                "${codeOracle.text}",
                "${descOracle.text}",
            )
            checkInitializedView(modelRequestAddProduct)
        }

        binding.btnChooseGalery.setOnClickListener {
            getImageFromGallery()
        }

        binding.btnGetCapture.setOnClickListener {
            getImageCapture()
        }

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setInputError() {
        Toast.makeText(this, "Column is required", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun getImageCapture() {
        Toast.makeText(this, "get Image capture", Toast.LENGTH_SHORT).show()
    }

    override fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.resultImageFromGallery(requestCode, resultCode) {
           try {
               if (data != null) {
                   fillPath = data.data!!
                   binding.ivChooseImage.setImageURI(fillPath)
                   binding.btnChooseGalery.visibility = View.GONE
                   binding.btnGetCapture.visibility = View.GONE
               }
           }catch (e: Exception) {
           }
        }
    }



    override fun storeToDatabase() {

        Toast.makeText(this, "STORE TO DATABASE", Toast.LENGTH_SHORT).show()


//        // Retrieving Preference data
//        val imgUri = PreferenceHelper.loadData(this)
//        Log.d("MainActivity", "Pref retrieve data uri: $imgUri")



//        presenter.uploadImageToFirebase(firebaseStorage, fillPath,this)
//
//        // Retrieving Preference data
//        val imgUri = PreferenceHelper.loadData(this)
//
//        val product = ModelProduct(
//            image = "$imgUri",
//            code_items = binding.etCodeProduct.text.toString(),
//            name =  binding.etNameProduct.text.toString(),
//            qty = 20,
//            category = binding.etCategoryProduct.text.toString(),
//            sub_category = binding.etSubCategoryProduct.text.toString(),
//            specification = binding.etSpecProduct.text.toString(),
//            price = binding.etPriceProduct.text.toString().toInt(),
//            location = binding.etLocationProduct.text.toString(),
//            status =  binding.etStatusProduct.text.toString(),
//            model = binding.etModelProduct.text.toString(),
//            code_oracle = binding.etCodeOracleProduct.text.toString(),
//            description_oracle =  binding.etDescOracleProduct.text.toString(),
//        )
//        presenter.requestApiDataProduct(product, this)
    }

    private fun validateCheckInput(request: ModelProduct) {
        try {
            when {
                binding.etCodeProduct.text.isEmpty() -> binding.etCodeProduct.error = "Code product harus di isi"
                binding.etNameProduct.text.isEmpty() -> binding.etNameProduct.error = "Nama product harus di isi"
                binding.etCategoryProduct.text.isEmpty() -> binding.etCategoryProduct.error = "Category product harus di isi"
                binding.etSubCategoryProduct.text.isEmpty() -> binding.etSubCategoryProduct.error = "Sub Category product harus di isi"
                binding.etSpecProduct.text.isEmpty() -> binding.etSpecProduct.error = "Spec product harus di isi"
                binding.etPriceProduct.text.isEmpty() -> binding.etPriceProduct.error = "Price product harus di isi"
                binding.etLocationProduct.text.isEmpty() -> binding.etLocationProduct.error = "Location product harus di isi"
                binding.etStatusProduct.text.isEmpty() -> binding.etStatusProduct.error = "Status product harus di isi"
                binding.etModelProduct.text.isEmpty() -> binding.etModelProduct.error = "Model product harus di isi"
                binding.etCodeOracleProduct.text.isEmpty() -> binding.etCodeOracleProduct.error = "Code Oracle product harus di isi"
                binding.etDescOracleProduct.text.isEmpty() -> binding.etDescOracleProduct.error = "Desc Oracle product harus di isi"
                else -> {
                    Toast.makeText(this, "CEK VALIDASI INPUT BERHASIL", Toast.LENGTH_SHORT).show()

                    // path gambar
                    val path = fillPath

                    // upload path ke firebase storage
                    uploadImageToFirebaseStorage(path)
                }
            }
//            presenter.validateAddProduct(request)
        }catch (e: Exception) {
            Log.d("ProductActivity","Validate Check Error: ${e.message}")
        }
    }

    private fun checkInitializedView(request: ModelProduct) {
        if (this::fillPath.isInitialized) {
            validateCheckInput(request)
        } else {
            Toast.makeText(this, "Validate Error: Gambar harus dilampirkan!", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImageToFirebaseStorage(uriPath: Uri) {
        val fillName = "${codeOracle.text}_${UUID.randomUUID()}.jpg"
        val refStorage =
            firebaseStorage.reference.child("/image_product/$fillName")
        refStorage.putFile(uriPath).addOnSuccessListener { uploadTask ->

            Toast.makeText(this, "BERHASIL UPLOAD IMAGE KE STORAGE", Toast.LENGTH_SHORT).show()

            // Mendapatkan uri image yang telah di upload
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                // Cek data yang mau di kirim
                Log.d("MainActivity", modelRequestAddProduct.toString())

                // Assigment data image menjadi uri
                modelRequestAddProduct.image = uri.toString()

                Log.d("MainActivity", modelRequestAddProduct.toString())

                // Store data jika sudah benar semua
                presenter.requestApiDataProduct(modelRequestAddProduct, this)

//                // Save Preference data
//                // save image uri in preference
//                PreferenceHelper.saveData(context, uri.toString())


            }.addOnFailureListener {
                Log.e("MainActivity", "URI Failure: ${it.message}")
            }
        }.addOnFailureListener {
            Log.e("MainActivity", "URI Failure Task: ${it.message}")
        }
    }
}