package com.example.warehouseproject.core.view.product.add_product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.warehouseproject.core.constant.Constant.REQUEST_CODE
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.view.product.ModelProduct
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductActivity : AppCompatActivity(), AddProductView {
    private lateinit var modelRequestAddProduct: ModelProduct


//    View
    private lateinit var code: EditText
    private lateinit var name: EditText
    private lateinit var qty: EditText
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
        qty = binding.etQtyProduct
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
                "${qty.text}",
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
            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)
        }

        binding.btnChooseGalery.setOnClickListener {
            getImageFromGallery()
        }

        binding.btnChooseGaleryChange.setOnClickListener {
            getImageFromGallery()
        }

        binding.btnGetCapture.setOnClickListener {
            getImageCapture()
        }

    }

    override fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun navigateToHome() {
        finish()
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
                    binding.btnChooseGaleryChange.visibility = View.VISIBLE
                }
            }catch (e: Exception) {
            }
        }
    }

    override fun showInputErrorCode() {
        code.error = "Code product harus di isi"
        code.requestFocus()
    }

    override fun showInputErrorName() {
        name.error = "Nama product harus di isi"
        name.requestFocus()
    }

    override fun showInputErrorQty() {
        qty.error = "Quantity product harus di isi"
        qty.requestFocus()
    }

    override fun showInputErrorCategory() {
      category.error = "Category product harus di isi"
        category.requestFocus()
    }

    override fun showInputErrorSubCategory() {
        subCategory.error = "Sub Category product harus di isi"
        subCategory.requestFocus()
    }

    override fun showInputErrorSpec() {
        spec.error = "Spec product harus di isi"
        spec.requestFocus()
    }

    override fun showInputErrorPrice() {
        price.error = "Price product harus di isi"
        price.requestFocus()
    }

    override fun showInputErrorLocation() {
        location.error = "Location product harus di isi"
        location.requestFocus()
    }

    override fun showInputErrorStatus() {
       status.error = "Status product harus di isi"
        status.requestFocus()
    }

    override fun showInputErrorModel() {
        model.error = "Model product harus di isi"
        model.requestFocus()
    }

    override fun showInputErrorCodeOracle() {
       codeOracle.error = "Code Oracle product harus di isi"
        codeOracle.requestFocus()
    }

    override fun showInputErrorDescOracle() {
      descOracle.error = "Desc Oracle product harus di isi"
        descOracle.requestFocus()
    }

    override fun showButton() {
        binding.submitButtonAddProduct.visibility = View.VISIBLE
    }

    override fun hideButton() {
        binding.submitButtonAddProduct.visibility = View.GONE
    }

    override fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showSuccessValidation() {
        Toast.makeText(this, "VALIDASI INPUT SUKSES", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "SEDANG UPLOAD DATA TO DATABASE", Toast.LENGTH_SHORT).show()

        val nameFile = UUID.randomUUID()

        val refStorage = firebaseStorage.reference.child("/image_product/${codeOracle.text}_${nameFile}.jpg")
        refStorage.putFile(fillPath).addOnSuccessListener { uploadTask ->

            // Mendapatkan uri image yang telah di upload
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                modelRequestAddProduct.image = uri.toString()
                presenter.requestApiDataProduct(modelRequestAddProduct, this)

            }.addOnFailureListener {
                Toast.makeText(this, "DOWNLOAD PHOTO GAGAL!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "UPLOAD PHOTO GAGAL!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getImageCapture() {
        Toast.makeText(this, "get Image capture", Toast.LENGTH_SHORT).show()
    }

    private fun checkInitializedView(request: ModelProduct) {
        if (this::fillPath.isInitialized) {
            presenter.validateAddProduct(request)
        } else {
            Toast.makeText(this, "Validate Error: Gambar harus dilampirkan!", Toast.LENGTH_SHORT).show()
        }
    }
}