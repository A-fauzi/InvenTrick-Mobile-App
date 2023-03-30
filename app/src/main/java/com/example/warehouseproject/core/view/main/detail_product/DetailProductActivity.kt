package com.example.warehouseproject.core.view.main.detail_product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import coil.load
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.helper.Currency
import com.example.warehouseproject.core.utils.helper.QrCode
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.databinding.ActivityDetailProductBinding
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import io.paperdb.Paper

class DetailProductActivity : AppCompatActivity(), DetailProductView {
    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var presenter: DetailProductPresenter
    private lateinit var apiService: ProductApiService

    val token = Paper.book().read<String>(Constant.User.TOKEN).toString()

    private fun initView() {
        Paper.init(this)
        apiService = ProductApiService(token)
        presenter = DetailProductPresenter(this, apiService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onStart() {
        super.onStart()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val bundle = intent.extras
        val productId = bundle?.getString("id")
        val image = bundle?.getString("image")
        val name = bundle?.getString("name")
        val code = bundle?.getString("code")
        val spec = bundle?.getString("spec")
        val exp = bundle?.getString("exp")
        val qty = bundle?.getString("qty")
        val price = bundle?.getString("price")
        val location = bundle?.getString("location")
        val model = bundle?.getString("model")
        val lot = bundle?.getString("lot")
        val createAt = bundle?.getString("createAt")
        val updateAt = bundle?.getString("updateAt")
        val category = bundle?.getString("category")
        val subCat = bundle?.getString("subcat")
        val status = bundle?.getString("status")
        val uid = bundle?.getString("uid")
        val pathStorage = bundle?.getString("path")

        // Set product data to view
        setTextProductData(
            qty, location, code, name,
            createAt, updateAt, category, subCat,
            spec, lot, model, exp
        )

        // set image product
        binding.ivBackdropCollapse.load(image) {
            crossfade(true)
            placeholder(R.drawable.ic_people)
        }

        // Encoder qrcode
        if (code != null) {
            QrCode.generate(code, binding.ivQrCode)
        }

        binding.tvCodeItem.text = code

        if (price != null) {
            binding.tvPriceDetail.text = Currency.format(price.toDouble(), "id", "ID")
        }

        statusProduct(status)

        checkingProductUser(uid)

        checkingUserPosition()

        onClickButton(code, pathStorage, productId)

        binding.tvPrintBarcode.setOnClickListener {

            val barcode = binding.cvContentBarcode

            PdfGenerator.getBuilder()
                .setContext(this)
                .fromViewSource()
                .fromView(barcode)
                .setFileName("BARCODE_PRODUCT")
                .setFolderNameOrPath("warehouse_barcode/")
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.NONE)
                .build(object : PdfGeneratorListener(){
                    override fun onStartPDFGeneration() {
                    }

                    override fun onFinishPDFGeneration() {
                    }

                    override fun onFailure(failureResponse: FailureResponse?) {
                        super.onFailure(failureResponse)
                        Toast.makeText(this@DetailProductActivity, "onFailure: " + failureResponse?.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(response: SuccessResponse?) {
                        super.onSuccess(response)
                        Toast.makeText(this@DetailProductActivity, "Success ${response?.path}", Toast.LENGTH_SHORT).show()
                    }

                })
        }

    }

    private fun onClickButton(
        code: String?,
        pathStorage: String?,
        productId: String?
    ) {
        val bundle2 = Bundle()
        bundle2.putString("code_items_key", code)
        binding.btnProdIn.setOnClickListener {
            val intent = Intent(this@DetailProductActivity, StockInActivity::class.java)
            intent.putExtras(bundle2)
            startActivity(intent)
        }
        binding.btnProdOut.setOnClickListener {
            val intent = Intent(this@DetailProductActivity, StockOutActivity::class.java)
            intent.putExtras(bundle2)
            startActivity(intent)
        }

        binding.tvBtnTrash.setOnClickListener {

            MaterialAlertDialogBuilder(
                this@DetailProductActivity,
                R.style.ThemeOverlay_App_MaterialAlertDialog
            )
                .setTitle("Delete item!")
                .setMessage("Are u sure this delete item?")
                .setCancelable(true)
                .setNegativeButton("Decline") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Accept") { dialog, which ->
                    deletePathStorage(pathStorage, productId)
                }
                .show()
        }
        binding.tvBtnUpdate.setOnClickListener {
            Toast.makeText(
                this@DetailProductActivity,
                "Belum bisa digunakan",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deletePathStorage(pathStorage: String?, productId: String?) {
        if (pathStorage != null) {
            val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
            val refStorageDelete = firebaseStorage.reference.child(pathStorage)
            refStorageDelete.delete().addOnSuccessListener {
                binding.progressDelete.visibility = View.VISIBLE
                presenter.deleteProductService(productId)
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@DetailProductActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkingUserPosition() {
        val positionCurrentUser = Paper.book().read<String>(Constant.User.DIVISION).toString()
        if (positionCurrentUser == "Receiving") {
            binding.btnProdOut.visibility = View.GONE
        } else if (positionCurrentUser == "Finish Good") {
            binding.btnProdIn.visibility = View.GONE
        }
    }

    private fun checkingProductUser(uid: String?) {
        val idCurrentUser = Paper.book().read<String>(Constant.User.ID).toString()
        if (uid != idCurrentUser) {
            binding.tvBtnTrash.visibility = View.GONE
            binding.tvBtnUpdate.visibility = View.GONE

    //            binding.btnProdOut.visibility = View.GONE
    //            binding.btnProdIn.visibility = View.GONE
        }
    }

    private fun setTextProductData(
        qty: String?,
        location: String?,
        code: String?,
        name: String?,
        createAt: String?,
        updateAt: String?,
        category: String?,
        subCat: String?,
        spec: String?,
        lot: String?,
        model: String?,
        exp: String?
    ) {
        binding.tvQtyDetail.text = qty
        binding.tvLocationDetail.text = location
        binding.chipCodeItemDetail.text = code
        binding.tvNameProductDetail.text = name
        binding.chipCatgory.text = category
        binding.chipSubCategory.text = subCat
        binding.tvSpecProductDetail.text = spec
        binding.tvLotDetail.text = lot
        binding.tvModelDetail.text = model
        binding.tvExpDetail.text = exp
    }

    private fun statusProduct(status: String?) {
        binding.statusDetail.text = status
        when (status) {
            "active" -> {
                binding.statusDetail.chipBackgroundColor =
                    getColorStateList(R.color.green_cendol)
            }
            "in-progress" -> {
                binding.statusDetail.chipBackgroundColor = getColorStateList(R.color.red_smooth)
            }
            else -> {
                binding.statusDetail.chipBackgroundColor = getColorStateList(R.color.blue)
            }
        }
    }

    override fun onResponseSuccessBody(msg: String, data: Product?) {
        Toast.makeText(
            this@DetailProductActivity,
            msg,
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(
            this@DetailProductActivity,
            MainActivity::class.java
        )
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onResponseErrorBody(msg: String) {
        Toast.makeText(
            this@DetailProductActivity,
            msg,
            Toast.LENGTH_SHORT
        ).show()
        binding.progressDelete.visibility = View.GONE
    }

    override fun onFailureDeleteProduct(msg: String) {
        Toast.makeText(
            this@DetailProductActivity,
            msg,
            Toast.LENGTH_SHORT
        ).show()
        binding.progressDelete.visibility = View.GONE
    }
}