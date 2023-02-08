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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityStockInBinding
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso

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
        inputCodeProduct = binding.etInputCodeProduct
        textDataEmpty = binding.tvDataIsEmpty
        progressBar = binding.progressBar
        inputQtyProduct = binding.etQtyProduct
        cardFullContent = binding.cardFullContent
        animationView = binding.animationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.btnSearchProduct.setOnClickListener {

            // On Click search
            animationView.setAnimation(R.raw.product_search)
            animationView.loop(true)
            animationView.playAnimation()

            textDataEmpty.text = "Mencari item..."

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val code = inputCodeProduct.text.toString()

            // call presenter search product
            presenter.searchProduct(code)


        }

        // on Click submit stock
        binding.btnSubmitStockIn.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val qtyInput = inputQtyProduct.text.toString()

            val resultCalculate = beforeQty + qtyInput.toInt()

            val qty = ProductRequest.RequestQtyOnly(resultCalculate.toString())

            // call presenter update qty
            presenter.updateProductQty(this, binding.tvIdProduct.text.toString(), qty)


//            // update product qty
//            ProductApiService().updateProductQty(this, binding.tvIdProduct.text.toString(), qty) { message, data ->
//
//                // On Response success
//
//            }

        }
    }

    override fun getResultDataOnRest(data: Product) {
        beforeQty = data.qty.toInt()

        Picasso.get().load(data.image).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemProduct)
        binding.tvIdProduct.text = data._id
        binding.chipStatus.text = data.status
        binding.tvCodeItem.text = data.code_items
        binding.tvNameProduct.text = data.name
        binding.etQtyProduct.hint = data.qty
    }

    override fun showViewOnSuccessResponse() {
        inputCodeProduct.text?.clear()
        cardFullContent.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun showViewOnErrorResponse(msg: String) {
        animationView.setAnimation(R.raw.product_not_found)
        animationView.loop(true)
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
        val dataRequest = StockHistory.StockHistoryRequest(data.code_items, data.name, inputQtyProduct.text.toString(), "IN")
        ProductApiService().createStockHistory( dataRequest)

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
}