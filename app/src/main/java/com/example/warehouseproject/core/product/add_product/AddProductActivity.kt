package com.example.warehouseproject.core.product.add_product

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
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : AppCompatActivity(), AddProductView {
    private lateinit var modelRequestAddProduct: ModelRequestAddProduct

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
            modelRequestAddProduct = ModelRequestAddProduct(
                "${code.text}",
                "${name.text}",
                "${category.text}",
                "${subCategory.text}",
                "https://ibb.co/XZ8hcVm",
                "${spec.text}",
                price.text.toString().toInt(),
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

//        // Retrieving Preference data
//        val imgUri = PreferenceHelper.loadData(this)
//        Log.d("MainActivity", "Pref retrieve data uri: $imgUri")



        presenter.uploadImageToFirebase(firebaseStorage, fillPath,this)

        // Retrieving Preference data
        val imgUri = PreferenceHelper.loadData(this)

        val product = ModelRequestAddProduct(
            image = "$imgUri",
            code_items = binding.etCodeProduct.text.toString(),
            name =  binding.etNameProduct.text.toString(),
            category = binding.etCategoryProduct.text.toString(),
            sub_category = binding.etSubCategoryProduct.text.toString(),
            specification = binding.etSpecProduct.text.toString(),
            price = binding.etPriceProduct.text.toString().toInt(),
            location = binding.etLocationProduct.text.toString(),
            status =  binding.etStatusProduct.text.toString(),
            model = binding.etModelProduct.text.toString(),
            code_oracle = binding.etCodeOracleProduct.text.toString(),
            description_oracle =  binding.etDescOracleProduct.text.toString(),
        )

        presenter.requestApiDataProduct(product, this)
    }

    private fun validateCheckInput(request: ModelRequestAddProduct) {
        try {
            presenter.validateAddProduct(request)
        }catch (e: Exception) {
            Log.d("MainActivity","Validate Check Error: ${e.message}")
        }
    }

    private fun checkInitializedView(request: ModelRequestAddProduct) {
        if (this::fillPath.isInitialized) {
            validateCheckInput(request)
        } else {
            Toast.makeText(this, "Validate Error: Image is required!", Toast.LENGTH_SHORT).show()
        }
    }
}