package com.example.warehouseproject.core.view.product.add_product.steps

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.model.user.User
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.service.product.category.ProductCategoryService
import com.example.warehouseproject.core.utils.helper.Currency
import com.example.warehouseproject.core.utils.helper.DatePickerDialog
import com.example.warehouseproject.core.utils.helper.RandomCodeProduct
import com.example.warehouseproject.core.utils.helper.TextWatcher.addTextCangedListener
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductPresenter
import com.example.warehouseproject.core.view.product.add_product.AddProductView
import com.example.warehouseproject.databinding.ActivityAddProductStepBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import io.paperdb.Paper
import java.io.ByteArrayOutputStream
import java.util.*

class AddProductStepActivity : AppCompatActivity(), AdapterCategoryAddProduct.AdapterCategoryListener, AddProductView {

    private lateinit var binding: ActivityAddProductStepBinding
    private lateinit var adapterCategory: AdapterCategoryAddProduct
    private lateinit var adapterSubCategory: AdapterSubCategoryAddProduct
    private lateinit var productCategoryService: ProductCategoryService
    private lateinit var fillPath: Uri
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var modelRequestAddProduct: ProductRequest
    private lateinit var modelUser: User
    private lateinit var presenter: AddProductPresenter

    private var category = "null"
    private var subCategory = "null"
    private var progressPercentace = 5


    private val token = Paper.book().read<String>(Constant.User.TOKEN).toString()
    private fun initView() {
        Paper.init(this)
        firebaseStorage = FirebaseStorage.getInstance()
        adapterCategory = AdapterCategoryAddProduct(this, arrayListOf(), this)
        adapterSubCategory = AdapterSubCategoryAddProduct(this, arrayListOf())
        presenter = AddProductPresenter(this, ProductApiService(token))

        val token = Paper.book().read<String>(Constant.User.TOKEN).toString()
        productCategoryService = ProductCategoryService(token)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductStepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        binding.baseCardview2.isEnabled = false
        binding.baseCardview2.setStrokeColor(getColorStateList(com.google.android.material.R.color.material_on_surface_disabled))
        binding.baseCardview3.isEnabled = false
        binding.baseCardview3.setStrokeColor(getColorStateList(com.google.android.material.R.color.material_on_surface_disabled))

        autoCompleteStatusProduct()

    }

    override fun onStart() {
        super.onStart()

        // Text watcher on validation form
        binding.etProductName.addTextChangedListener(textWatcher(binding.etProductName))
        binding.etProductSpec.addTextChangedListener(textWatcher(binding.etProductSpec))
        binding.etProductLocation.addTextChangedListener(textWatcher(binding.etProductLocation))
        binding.etProductModel.addTextChangedListener(textWatcher(binding.etProductModel))
        binding.etProductLot.addTextChangedListener(textWatcher(binding.etProductLot))

        // card component expandable
        viewExpandable(
            btnShow = binding.show,
            cardGroup = binding.cardGroup,
            baseCard = binding.baseCardview1
        )
        viewExpandable(
            btnShow = binding.show2,
            cardGroup = binding.cardGroup2,
            baseCard = binding.baseCardview2
        )
        viewExpandable(
            btnShow = binding.show2View3,
            cardGroup = binding.cardGroup2View3,
            baseCard = binding.baseCardview3
        )

        productCategoryService.getCategories {
            adapterCategory.setData(it)
        }

        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCategory
        }

        binding.cvBtnSaveCard2.setOnClickListener {
            binding.imageView.setColorFilter(ContextCompat.getColor(this, R.color.green))
            progressPercentace = 66
            binding.GreenProgressBar.progress = progressPercentace
            binding.baseCardview3.isEnabled = true
            binding.baseCardview3.setStrokeColor(getColorStateList(R.color.blue_ocean))

            if (binding.cardGroup2.visibility == View.VISIBLE) {
                binding.cardGroup2.visibility = View.GONE
                binding.show2.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                binding.cardGroup2.visibility = View.VISIBLE
                binding.show2.setImageResource(android.R.drawable.arrow_up_float)
            }

        }

        binding.btnExpDate.setOnClickListener {
            datePickerDialog()
        }

        binding.btnNextForm.isEnabled = false
        binding.cvBtnSaveCard3.setOnClickListener {
            if (binding.tvResultExpDate.text == "null") {
                Toast.makeText(this, "Set tanggal exp diperlukan!", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnNextForm.isEnabled = true
                binding.imageViewView3.setColorFilter(ContextCompat.getColor(this, R.color.green))
                progressPercentace = 100
                binding.GreenProgressBar.progress = progressPercentace
            }
        }


        binding.tvChipCode.text = RandomCodeProduct.generate()

        addTextCangedListener(binding.etProductPrice) {
            inputPriceInteractor(it)
        }

        cardViewUploadImage()

        resultData()
    }

    /**
     * fungsi untuk menampilkan angka dengan format currency
     */
    private fun inputPriceInteractor(it: CharSequence?) {
        if (binding.etProductPrice.text.toString().isEmpty()) {
            binding.outlineTextfieldProductPrice.isHelperTextEnabled = true
            binding.outlineTextfieldProductPrice.helperText = "price is required!"
            binding.outlineTextfieldProductPrice.setHelperTextColor(getColorStateList(R.color.red_smooth))
            binding.outlineTextfieldProductLocation.isEnabled = false
        } else {
            binding.outlineTextfieldProductPrice.isHelperTextEnabled = true
            binding.outlineTextfieldProductPrice.helperText = Currency.format(it.toString().toDouble(), "id", "ID")
            binding.outlineTextfieldProductPrice.setHelperTextColor(getColorStateList(R.color.black))
            binding.outlineTextfieldProductLocation.isEnabled = true
        }
    }

    /**
     * Menampilkan sebuah dialog MaterialAlertDialogBuilder dengan tiga pilihan
     * untuk mengunggah gambar produk. Pilihan tersebut antara lain: dari galeri,
     * dari kamera, atau membatalkan pengunggahan.
     *
     * Ketika tombol "From Gallery" ditekan, fungsi getImageFromGallery() akan dipanggil.
     * Ketika tombol "From Camera" ditekan, akan ditampilkan sebuah pesan Toast yang
     * memberitahu bahwa fitur tersebut masih dalam pengembangan.
     */
    private fun cardViewUploadImage() {
        // Mendapatkan tombol upload dari layout binding
        val btn = binding.btnUpload.btnComponent

        // Mengatur teks dan mengaktifkan tombol upload
        btn.text = getString(R.string.upload)
        btn.isEnabled = true

        btn.setOnClickListener {
            getImageFromGallery()
        }
    }


    /**
     * Fungsi ini digunakan untuk membuka galeri dan memilih gambar.
     * Setelah memilih gambar, fungsi onActivityResult akan dijalankan dengan request code yang sama dengan Constant.REQUEST_CODE.
     */
    private fun getImageFromGallery() {
        // Membuat intent untuk membuka galeri
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        // Memulai aktivitas untuk memilih gambar dari galeri dan menunggu hasil dengan request code Constant.REQUEST_CODE
        startActivityForResult(intent, Constant.REQUEST_CODE)
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_CODE) {
            try {
                if (data != null) {

                    fillPath = data.data!!
                    Glide.with(this).load(fillPath).centerCrop().error(R.drawable.img_example)
                        .into(binding.ivChooseImage)
                    binding.imageView2.setColorFilter(ContextCompat.getColor(this, R.color.green))
                    progressPercentace = 33
                    binding.GreenProgressBar.progress = progressPercentace
                    binding.baseCardview2.isEnabled = true
                    binding.baseCardview2.setStrokeColor(getColorStateList(R.color.blue_ocean))
                } else {
                    Toast.makeText(this, "Data photo is null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Menampilkan sebuah DatePickerDialog dengan judul "exp date" (expiry date).
     * Ketika pengguna menekan tombol "OK" pada DatePickerDialog, maka tanggal yang
     * dipilih akan ditampilkan pada sebuah TextView yang diperoleh dari layout binding.
     */
    private fun datePickerDialog() {
        // Membuat sebuah instance DatePickerDialog
        val datePicker = DatePickerDialog(this)

        // Menetapkan judul DatePickerDialog menjadi "exp date"
        datePicker.setTextTitleDate("exp date") { datePicker ->
            // Menampilkan DatePickerDialog menggunakan supportFragmentManager
            datePicker.show(supportFragmentManager, "tag")

            // Menambahkan aksi klik tombol "OK" pada DatePickerDialog
            datePicker.addOnPositiveButtonClickListener {
                // Menampilkan tanggal yang dipilih pada sebuah TextView yang diperoleh dari layout binding
                binding.tvResultExpDate.text = datePicker.headerText
            }
        }
    }


    /**
     * Fungsi ini digunakan untuk menampilkan atau menyembunyikan konten expandable saat card ditekan.
     * @param btnShow adalah ImageView yang digunakan untuk menampilkan icon panah atas atau bawah tergantung pada kondisi expandable.
     * @param cardGroup adalah Group yang berisi konten expandable yang ingin ditampilkan atau disembunyikan.
     * @param baseCard adalah MaterialCardView yang digunakan sebagai trigger untuk menampilkan atau menyembunyikan konten expandable.
     */
    private fun viewExpandable(btnShow: ImageView, cardGroup: Group, baseCard: MaterialCardView) {
        baseCard.setOnClickListener {
            // Jika cardGroup sudah terlihat, maka sembunyikan konten expandable dan ubah icon menjadi panah bawah.
            if (cardGroup.visibility == View.VISIBLE) {
                cardGroup.visibility = View.GONE
                btnShow.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                // Jika cardGroup belum terlihat, maka tampilkan konten expandable dan ubah icon menjadi panah atas.
                cardGroup.visibility = View.VISIBLE
                btnShow.setImageResource(android.R.drawable.arrow_up_float)
            }
        }
    }


    /**
     * Fungsi ini digunakan untuk membuat dropdown autocomplete pada input status produk.
     * Data yang diinputkan adalah array list berisi status produk yang tersedia.
     */
    private fun autoCompleteStatusProduct() {
        // Deklarasi dan inisialisasi array list yang berisi status produk
        val array = arrayListOf("in-progress","active")

        // Deklarasi dan inisialisasi array adapter untuk menampilkan array list pada dropdown
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown_category, array)

        // Mengatur adapter pada dropdown autocomplete pada layout binding.autoCompleteStatus
        binding.autoCompleteStatus.setAdapter(arrayAdapter)
    }


    override fun onClickItem(category: Category) {
        this.category = category.name.toString()
        category.sub_category?.forEach {
            this.subCategory = it.name.toString()
        }

        category.sub_category?.let { adapterSubCategory.setData(it) }

        binding.rvSubCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterSubCategory
        }
    }

    private fun textWatcher(input: EditText) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when (input) {
                binding.etProductName -> {
                    inputValidation(
                        text = text,
                        currentInputLayout = binding.outlineTextfieldProductName,
                        nextInputLayout = binding.outlineTextfieldProductSpec,
                        helperText = "Product name tidak boleh kosong!"
                    )
                }
                binding.etProductSpec -> {
                    inputValidation(
                        text = text,
                        currentInputLayout = binding.outlineTextfieldProductSpec,
                        nextInputLayout = binding.outlinedTextFieldStatusProduct,
                        helperText = "Product specification tidak boleh kosong!",
                        viewOnTextIsNotEmpty = {
                            binding.cvBtnSaveCard2.visibility = View.VISIBLE
                        },
                        viewOnTextIsEmpty = {
                            binding.cvBtnSaveCard2.visibility = View.GONE
                        }
                    )
                }

                binding.etProductLocation -> {
                    inputValidation(
                        text = text,
                        currentInputLayout = binding.outlineTextfieldProductLocation,
                        nextInputLayout = binding.outlineTextfieldProductModel,
                        helperText = "Product location tidak boleh kosong!"
                    )
                }
                binding.etProductModel -> {
                    inputValidation(
                        text = text,
                        currentInputLayout = binding.outlineTextfieldProductModel,
                        nextInputLayout = binding.outlineTextfieldProductLot,
                        helperText = "Product model tidak boleh kosong!"
                    )
                }
                binding.etProductLot -> {
                    inputValidation(
                        text = text,
                        currentInputLayout = binding.outlineTextfieldProductLot,
                        nextInputLayout = null,
                        helperText = "Product name tidak boleh kosong!",
                        viewOnTextIsNotEmpty = {
                            binding.btnExpDate.isEnabled = true
                            binding.cvBtnSaveCard3.visibility = View.VISIBLE
                        },
                        viewOnTextIsEmpty = {
                            binding.btnExpDate.isEnabled = false
                            binding.cvBtnSaveCard3.visibility = View.GONE
                        }
                    )
                }
            }
        }

    }

    /**
     * Fungsi inputValidation digunakan untuk melakukan validasi pada inputan teks dari pengguna pada TextInputLayout. Fungsi ini memiliki beberapa parameter:

    text: String, teks inputan yang akan divalidasi.

    currentInputLayout: TextInputLayout, TextInputLayout saat ini yang akan divalidasi.

    nextInputLayout: TextInputLayout?, TextInputLayout berikutnya yang akan diaktifkan jika inputan saat ini valid.
    Parameter ini dapat berupa null jika tidak ada TextInputLayout berikutnya.

    helperText: String, teks bantuan yang akan ditampilkan jika inputan saat ini tidak valid.

    viewOnTextIsNotEmpty: (() -> Unit)? = null, aksi yang akan dijalankan jika inputan saat ini tidak kosong.
    Parameter ini dapat berupa null jika tidak ada aksi yang akan dijalankan.

    viewOnTextIsEmpty: (() -> Unit)? = null, aksi yang akan dijalankan jika inputan saat ini kosong.
    Parameter ini dapat berupa null jika tidak ada aksi yang akan dijalankan.

    Jika inputan saat ini tidak kosong, maka TextInputLayout saat ini akan diberi tanda END_ICON_CUSTOM, bantuan teks dinonaktifkan, TextInputLayout berikutnya diaktifkan, dan aksi pada viewOnTextIsNotEmpty dijalankan (jika ada).
    Jika inputan saat ini kosong, maka TextInputLayout saat ini akan diberi tanda END_ICON_NONE, bantuan teks ditampilkan, TextInputLayout berikutnya dinonaktifkan, dan aksi pada viewOnTextIsEmpty dijalankan (jika ada).
     */
    private fun inputValidation(
        text: String,
        currentInputLayout: TextInputLayout,
        nextInputLayout: TextInputLayout?,
        helperText: String,
        viewOnTextIsNotEmpty: (() -> Unit)? = null,
        viewOnTextIsEmpty: (() -> Unit)? = null,
    ) {
        if (text.isNotEmpty()) {
            currentInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            currentInputLayout.isHelperTextEnabled = false
            nextInputLayout?.isEnabled = true
            viewOnTextIsNotEmpty?.let { it() }
        } else {
            currentInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
            currentInputLayout.isHelperTextEnabled = true
            currentInputLayout.helperText = helperText
            currentInputLayout.setHelperTextColor(getColorStateList(R.color.red_smooth))
            nextInputLayout?.isEnabled = false
            viewOnTextIsEmpty?.let { it() }
        }
    }

    private fun resultData() {

        binding.btnNextForm.setOnClickListener {
            if (this::fillPath.isInitialized) {

                // onClick next button
                binding.btnNextForm.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE

                // Data product
                val fillPath = fillPath.toString()
                val code = binding.tvChipCode.text.toString()
                val name = binding.etProductName.text.toString()
                val spec = binding.etProductSpec.text.toString()
                val status = binding.autoCompleteStatus.text.toString()
                val category = category
                val subCategory = subCategory
                val quantity = binding.etProductQty.text.toString()
                val price = binding.etProductPrice.text.toString().toInt()
                val location = binding.etProductLocation.text.toString()
                val model = binding.etProductModel.text.toString()
                val lot = binding.etProductLot.text.toString()
                val expDate = binding.tvResultExpDate.text.toString()

                // Data user
                val uid = Paper.book().read<String>(Constant.User.ID).toString()
                val uName = Paper.book().read<String>(Constant.User.USERNAME).toString()
                val token = Paper.book().read<String>(Constant.User.TOKEN).toString()
                val profileImg = Paper.book().read<String>(Constant.User.PROFILE_PHOTO).toString()
                val fullName = Paper.book().read<String>(Constant.User.FULLNAME).toString()
                val email = Paper.book().read<String>(Constant.User.EMAIL).toString()
                val division = Paper.book().read<String>(Constant.User.DIVISION).toString()
                val storagePath = Paper.book().read<String>(Constant.User.STORAGE_PATH_PROFILE).toString()

                modelUser = User(
                    _id = uid, username =  uName, profile_image =  profileImg,
                    fullName =  fullName, email =  email, roles = arrayListOf(),
                    position = division, status_activity = "null", jwt_token = token,
                    path_storage = storagePath
                )
                modelRequestAddProduct = ProductRequest(
                    image = "null", code_items = code, name = name,
                    user = modelUser, qty = quantity, price = price,
                    category = category, sub_category = subCategory, specification = spec,
                    location = location, status = status, model = model,
                    lot = lot, exp = expDate, path_storage = "null"
                )

                // Upload image to firebase
                uploadImageToFirebase(code, name, fillPath)


            } else {
                Toast.makeText(this, "Tambahin gambar dulu", Toast.LENGTH_SHORT).show()
                binding.btnNextForm.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun uploadImageToFirebase(
        code: String,
        name: String,
        fillPath: String
    ) {
        val nameFile = UUID.randomUUID()
        val refStorage =
            firebaseStorage.reference.child("/image_product/${code}/${name}_${nameFile}.jpg")

        // Compress image
        val reduceImage: ByteArray = bytes(fillPath)

        refStorage.putBytes(reduceImage).addOnSuccessListener { uploadTask ->
            modelRequestAddProduct.path_storage = refStorage.path

            uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->

                modelRequestAddProduct.image = uri.toString()
                presenter.addProductRequest(modelRequestAddProduct)

            }.addOnFailureListener {
                Toast.makeText(this, "DOWNLOAD PHOTO GAGAL!", Toast.LENGTH_SHORT).show()
                binding.btnNextForm.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }.addOnFailureListener {
            Toast.makeText(this, "UPLOAD PHOTO GAGAL!", Toast.LENGTH_SHORT).show()
            binding.btnNextForm.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun bytes(fillPath: String): ByteArray {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fillPath.toUri())
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val reduceImage: ByteArray = byteArrayOutputStream.toByteArray()
        return reduceImage
    }


    private fun alertDialogErrAddItem(msg: String) {
        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Error!")
            .setIcon(R.drawable.ic_icons8_cancel)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton("Ok") { dialog, which ->

            }
            .show()
    }

    override fun onResponseSuccessBodyAddProduct(msg: String, data: Product?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onResponseErrorBodyAddProduct(msg: String) {
        // Harus nya hapus image di firestore

        alertDialogErrAddItem(msg)
        binding.btnNextForm.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onFailureResponseAddProduct(msg: String) {
        // Harus nya hapus image di firestore

        alertDialogErrAddItem(msg)
        binding.btnNextForm.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }


}