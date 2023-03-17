package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.example.awesomedialog.*
import com.example.warehouseproject.R
import com.example.warehouseproject.core.utils.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityStockInBinding
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class StockInActivity : AppCompatActivity(), StockInView {

    private lateinit var animationView: LottieAnimationView

    private lateinit var binding: ActivityStockInBinding

    private lateinit var presenter: StockInPresenter

    private lateinit var inputCodeProduct: EditText
    private lateinit var textDataEmpty: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var inputQtyProduct: TextInputEditText
    private lateinit var cardFullContent: CardView

    private var beforeQty: Int = 0


    private fun initView() {
        inputCodeProduct = binding.stockIn.etInputCodeProduct
        textDataEmpty = binding.stockIn.tvDataIsEmpty
        progressBar = binding.stockIn.progressBar
        inputQtyProduct = binding.stockIn.etQtyProduct
        cardFullContent = binding.stockIn.cardFullContent
        animationView = binding.stockIn.animationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)

        // Init view
        initView()

        // init presenter
        presenter = StockInPresenter(this, StockInInteractor())
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

//        on init

        animationView.setAnimation(R.raw.product)
        animationView.loop(false)
        animationView.playAnimation()

        // data barcode receiver
        val codeItemBundle = intent.extras?.getString("code_items_key")
        inputCodeProduct.setText(codeItemBundle)

        binding.stockIn.btnSearchProduct.setOnClickListener {

            // On Click search
            animationView.setAnimation(R.raw.product_search)
            animationView.loop(true)
            animationView.playAnimation()

            textDataEmpty.text = "Mencari item..."

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val code = inputCodeProduct.text.toString()

            // call presenter search product
            presenter.searchProduct(this, code)


        }

        // on Click submit stock
        binding.stockIn.btnSubmitStockIn.setOnClickListener {

            if (binding.stockIn.etQtyProduct.text.toString().isEmpty()) {
                binding.stockIn.outlinedTextFieldQtyProduct.helperText = "is required!"
                binding.stockIn.outlinedTextFieldQtyProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
            } else {
                progressBar.visibility = View.VISIBLE

                HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

                val qtyInput = inputQtyProduct.text.toString()

                val resultCalculate = beforeQty + qtyInput.toInt()

                val qty = ProductRequest.RequestQtyOnly(resultCalculate.toString())

                // call presenter update qty
                presenter.updateProductQty(this, binding.stockIn.tvIdProduct.text.toString(), qty)
            }

        }
    }

    override fun getResultDataOnRest(data: Product) {
        beforeQty = data.qty.toInt()

        Picasso.get().load(data.image).centerCrop().resize(500, 500).error(R.drawable.img_example).into(binding.stockIn.ivItemProduct)
        binding.stockIn.tvIdProduct.text = data._id
        binding.stockIn.chipStatus.text = data.status
        binding.stockIn.tvCodeItem.text = data.code_items
        binding.stockIn.tvNameProduct.text = data.name
        binding.stockIn.etQtyProduct.hint = data.qty

    }

    override fun showViewOnSuccessResponse() {
        inputCodeProduct.text?.clear()
        cardFullContent.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        binding.stockIn.containerSearchProductView.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun showViewOnErrorResponse(msg: String) {
        animationView.setAnimation(R.raw.product_not_found)
        animationView.loop(false)
        animationView.playAnimation()

        animationView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        inputCodeProduct.text?.clear()
        textDataEmpty.visibility = View.VISIBLE
        cardFullContent.visibility = View.GONE
        textDataEmpty.text = "Data tidak di temukan"
    }

    @SuppressLint("SetTextI18n")
    override fun showViewOnSuccessUpdateQty(data: Product) {
        val token = Paper.book().read<String>("token").toString()
        val dataRequest = StockHistory.StockHistoryRequest(data.code_items, data.name, inputQtyProduct.text.toString(), "IN")
        ProductApiService(token).createStockHistory( dataRequest)

        binding.stockIn.containerSearchView.visibility = View.GONE
        binding.stockIn.tvDescInputCode.visibility = View.GONE
        cardFullContent.visibility = View.GONE
        progressBar.visibility = View.GONE

        animationView.setAnimation(R.raw.successful)
        animationView.loop(false)
        animationView.playAnimation()

        textDataEmpty.text = "Quantity product telah ditambahkan sejumlah ${inputQtyProduct.text.toString()}"

        Handler().postDelayed( {
            startActivity(Intent(this, MainActivity::class.java).setFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }, 2000 )
    }

    override fun showViewOnErrorUpdateQty(msg: String) {
        cardFullContent.visibility = View.GONE
        progressBar.visibility = View.GONE
        AwesomeDialog.build(this)
            .title("Error!", null, resources.getColor(R.color.red_smooth))
            .body(msg, null, resources.getColor(R.color.black))
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Ok", null, null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }
}