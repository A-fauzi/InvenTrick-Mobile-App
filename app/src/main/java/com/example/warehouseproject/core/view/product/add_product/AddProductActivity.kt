package com.example.warehouseproject.core.view.product.add_product

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.awesomedialog.*
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.REQUEST_CODE
import com.example.warehouseproject.core.helper.*
import com.example.warehouseproject.core.helper.Currency
import com.example.warehouseproject.core.helper.TextWatcher.addTextCangedListener
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.model.user.User
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import java.io.ByteArrayOutputStream
import java.util.*

class AddProductActivity : AppCompatActivity(), AddProductView {
    private lateinit var modelRequestAddProduct: ProductRequest
    private lateinit var modelUser: User


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
       status = binding.autoCompleteStatus
       model = binding.etModelProduct
       lot = binding.etLotProduct
       exp = binding.etExpProduct
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.getCategory(this)

        autoCompleteStatusProduct()

        initView()

        addTextCangedListener(code) { char ->
            presenter.searchItemsProductName(applicationContext, char.toString())
        }

        firebaseStorage = FirebaseStorage.getInstance()

        val uid = Paper.book().read<String>("id").toString()
        val uName = Paper.book().read<String>("username").toString()
        val token = Paper.book().read<String>("token").toString()
        val uPhoto = "null"
        binding.submitButtonAddProduct.setOnClickListener {
            modelUser = User(uid, uName, "null", "null", arrayListOf(), "null", "online", token)
            modelRequestAddProduct = ProductRequest(
                image = "Kosong",
                code_items = "${code.text}",
                name = "${name.text}",
                user = modelUser,
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
                path_storage = ""
            )
            checkInitializedView(modelRequestAddProduct)
            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)
        }

        binding.cvSelectExpDate.setOnClickListener {

            datePickerDialog()

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


        addTextCangedListener(price) {
            inputPriceInteractor(it)
        }


    }

    private fun inputPriceInteractor(it: CharSequence?) {
        if (price.text.toString().isEmpty()) {
            binding.outlinedTextFieldPriceProduct.helperText = "price is not empty!"
            binding.outlinedTextFieldPriceProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
        } else {
            binding.outlinedTextFieldPriceProduct.helperText = Currency.format(it.toString().toDouble(), "id", "ID")
            binding.outlinedTextFieldPriceProduct.setHelperTextColor(getColorStateList(R.color.black))
        }
    }

    private fun datePickerDialog() {
        DatePickerDialog(this).setTextTitleDate("exp date") { datePicker ->
            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                exp.setText(datePicker.headerText)
            }
        }
    }

    private fun autoCompleteStatusProduct() {
        val array = arrayListOf("active", "in-progress")
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown_category, array)
        binding.autoCompleteStatus.setAdapter(arrayAdapter)
    }

    override fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onDataCategoryRequestNotEmptyView(data: List<Category>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown_category, data.map { it.name })
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onDataCategoryRequestIsEmptyView() {
        binding.autoCompleteTextView.isEnabled = false
        binding.autoCompleteTextView.setText("Kosong")
        binding.txtInputLayoutCategory.helperText = "Category masih kosong, tambahkan nanti"
        binding.txtInputLayoutCategory.setHelperTextColor(getColorStateList(R.color.red_smooth))

        binding.outlinedTextFieldSubCategoryProduct.isEnabled = false
        binding.etSubCategoryProduct.setText("kosong")
        binding.outlinedTextFieldSubCategoryProduct.helperText = "Sub Category masih kosong, tambahkan nanti"
        binding.outlinedTextFieldSubCategoryProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))

    }

    override fun searchItemsIsNullView() {
        binding.etNameProduct.text?.clear()
        binding.outlinedTextFieldNameProduct.helperText = "Tidak ada data name dengan code produk ${code.text}"
        binding.outlinedTextFieldNameProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
        binding.etNameProduct.text?.clear()
        binding.outlinedTextFieldNameProduct.isEnabled = false
    }

    override fun searchItemsIsNotNullView(data: ProductModelAssets?) {
        binding.etNameProduct.setText(data?.itemNameDesc)
        binding.etNameProduct.requestFocus()
        binding.outlinedTextFieldNameProduct.isEnabled = true
        binding.outlinedTextFieldNameProduct.isHelperTextEnabled = false
        binding.outlinedTextFieldNameProduct.isEnabled = true
    }

    override fun onSuccessTryResultImageFromGalleryView(data: Intent?) {
        if (data != null) {
            fillPath = data.data!!
            Picasso.get().load(fillPath).centerCrop().resize(500, 500).error(R.drawable.img_example).into(binding.ivChooseImage)
            binding.btnChooseGalery.visibility = View.GONE
            binding.btnGetCapture.visibility = View.GONE
            binding.btnChooseGaleryChange.visibility = View.VISIBLE
        }
    }

    override fun onFailureCatchResultImageFromGalleryView(e: Exception) {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.resultImageFromGallery(requestCode, resultCode, data)
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

        refStorage.putBytes(reduceImage).addOnSuccessListener { uploadTask ->

            modelRequestAddProduct.path_storage = refStorage.path

            // Mendapatkan uri image yang telah di upload
            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                modelRequestAddProduct.image = uri.toString()
                presenter.requestApiDataProduct(this, modelRequestAddProduct, {msg, data ->
                    Toast.makeText(this, "$msg ${data?.name}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                }, { msg ->
                    AwesomeDialog.build(this)
                        .title("Error!", null, resources.getColor(R.color.red_smooth))
                        .body(msg, null, resources.getColor(R.color.black))
                        .position(AwesomeDialog.POSITIONS.BOTTOM)
                        .onPositive("Ok", null, null) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .icon(R.drawable.ic_icons8_cancel)
                    binding.progressBar.visibility = View.GONE
                    binding.submitButtonAddProduct.visibility = View.VISIBLE
                },
                    { msg ->
                        AwesomeDialog.build(this)
                            .title("Error!", null, resources.getColor(R.color.red_smooth))
                            .body(msg, null, resources.getColor(R.color.black))
                            .position(AwesomeDialog.POSITIONS.BOTTOM)
                            .onPositive("Ok", null, null) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .icon(R.drawable.ic_icons8_cancel)
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
            AwesomeDialog.build(this)
                .title("Error!", null, resources.getColor(R.color.red_smooth))
                .body("Please input image", null, resources.getColor(R.color.black))
                .position(AwesomeDialog.POSITIONS.BOTTOM)
                .onNegative("OK")
                .icon(R.drawable.ic_icons8_cancel)
        }
    }
}