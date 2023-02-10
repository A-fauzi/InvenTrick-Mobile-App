package com.example.warehouseproject.core.view.product.add_product

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.REQUEST_CODE
import com.example.warehouseproject.core.helper.Currency
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.helper.PreferenceHelper
import com.example.warehouseproject.core.helper.PreferenceHelper.saveData
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.model.product.category.CategoryResponse
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.service.product.category.ProductCategoryService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.tapadoo.alerter.Alerter
import java.io.ByteArrayOutputStream
import java.io.File
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
       category = binding.autoCompleteTextView
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

        // tes api
        ProductCategoryService().getCategories{ data ->
            if (data.isNotEmpty()) {
                // Example category nanti
                val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown_category, data.map { it.name })
                binding.autoCompleteTextView.setAdapter(arrayAdapter)
            } else {
               binding.autoCompleteTextView.isEnabled = false
                binding.autoCompleteTextView.setText("Kosong")
                binding.txtInputLayoutCategory.helperText = "Category masih kosong, tambahkan nanti"
                binding.txtInputLayoutCategory.setHelperTextColor(getColorStateList(R.color.red_smooth))
            }
        }

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

        binding.cvSelectExpDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select exp date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, "tag ")
            datePicker.addOnPositiveButtonClickListener {
                exp.setText(datePicker.headerText)
            }
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
                    Picasso.get().load(fillPath).centerCrop().resize(500, 500).error(R.drawable.img_example).into(binding.ivChooseImage)
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

        val refStorage = firebaseStorage.reference.child("/image_product/${code.text}/${name.text}_${nameFile}.jpg")

//        // Compress image
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fillPath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val reduceImage: ByteArray = byteArrayOutputStream.toByteArray()

        // save data in shared
        saveData(this, refStorage.path, code.text.toString())

        refStorage.putBytes(reduceImage).addOnSuccessListener { uploadTask ->

            PreferenceHelper.saveData(this, refStorage.path, code.text.toString())

            // Mendapatkan uri image yang telah di upload
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                modelRequestAddProduct.image = uri.toString()
                presenter.requestApiDataProduct(modelRequestAddProduct, {msg, data ->
                    Toast.makeText(this, "$msg ${data?.name}", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(applicationContext, MainActivity::class.java))
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

    @SuppressLint("PrivateResource")
    private fun checkInitializedView(request: ProductRequest) {
        if (this::fillPath.isInitialized) {
            presenter.validateAddProduct(request)
        } else {
            Alerter.create(this@AddProductActivity)
                .setText("Gambar harus dilampirkan!")
                .setIcon(com.google.android.material.R.drawable.mtrl_ic_error)
                .setBackgroundColorRes(R.color.red_smooth)
                .show()
        }
    }
}