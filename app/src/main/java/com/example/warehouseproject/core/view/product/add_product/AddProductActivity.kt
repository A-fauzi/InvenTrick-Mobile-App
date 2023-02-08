package com.example.warehouseproject.core.view.product.add_product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.REQUEST_CODE
import com.example.warehouseproject.core.helper.Currency
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage
import com.tapadoo.alerter.Alerter
import java.util.*

class AddProductActivity : AppCompatActivity(), AddProductView {
    private lateinit var modelRequestAddProduct: ProductRequest


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
    private lateinit var lot: EditText
    private lateinit var exp: EditText

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
       lot = binding.etLotProduct
       exp = binding.etExpProduct
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()


        firebaseStorage = FirebaseStorage.getInstance()


        binding.submitButtonAddProduct.setOnClickListener {
            modelRequestAddProduct = ProductRequest(
                image = "Kosong",
                code_items = "${code.text}",
                name = "${name.text}",
                qty = "${qty.text}",
                price = price.text.toString(),
                category = "${category.text}",
                sub_category = "${subCategory.text}",
                specification = "${spec.text}",
                location = "${location.text}",
                status = "${status.text}",
                model = "${model.text}",
                lot = "${lot.text}",
                exp = "${exp.text}",
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

        val textWatcher =  object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (price.text.toString().isEmpty()) {
                    binding.outlinedTextFieldPriceProduct.helperText =  "is not empty!"
                    binding.outlinedTextFieldPriceProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
                } else {
                    binding.outlinedTextFieldPriceProduct.helperText =  Currency.format(p0.toString().toDouble(), "id", "ID")
                    binding.outlinedTextFieldPriceProduct.setHelperTextColor(getColorStateList(R.color.black))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        price.addTextChangedListener(textWatcher)


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

    override fun showInputErrorLot() {
       lot.error = "Code Oracle product harus di isi"
        lot.requestFocus()
    }

    override fun showInputErrorExp() {
      exp.error = "Desc Oracle product harus di isi"
        exp.requestFocus()
    }

    override fun hideButton() {
        binding.submitButtonAddProduct.visibility = View.GONE
    }

    override fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun showSuccessValidation() {

        val nameFile = UUID.randomUUID()

        val refStorage = firebaseStorage.reference.child("/image_product/${lot.text}_${nameFile}.jpg")
        refStorage.putFile(fillPath).addOnSuccessListener { uploadTask ->

            // Mendapatkan uri image yang telah di upload
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                modelRequestAddProduct.image = uri.toString()
                presenter.requestApiDataProduct(modelRequestAddProduct, {msg, data ->
                    Toast.makeText(this, "$msg ${data?.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, { msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.submitButtonAddProduct.visibility = View.VISIBLE
                },
                    { msg ->
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        binding.submitButtonAddProduct.visibility = View.VISIBLE
                })

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

    private fun checkInitializedView(request: ProductRequest) {
        if (this::fillPath.isInitialized) {
            presenter.validateAddProduct(request)
        } else {
            Alerter.create(this@AddProductActivity)
                .setText("Gambar harus dilampirkan!")
                .setIcon(R.drawable.ic_baseline_warning_amber_24)
                .setBackgroundColorRes(R.color.yellow)
                .show()
        }
    }
}