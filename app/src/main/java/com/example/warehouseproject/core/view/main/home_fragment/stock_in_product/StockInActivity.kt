package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityStockInBinding
import com.squareup.picasso.Picasso

class StockInActivity : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView

    private lateinit var binding: ActivityStockInBinding

    private var beforeQty: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animationView = binding.animationView
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        animationView.setAnimation(R.raw.product)
        animationView.loop(false)
        animationView.playAnimation()

        val codeItemBundle = intent.extras?.getString("code_items_key")
            binding.etInputCodeProduct.setText(codeItemBundle)

        binding.btnSearchProduct.setOnClickListener {

            binding.animationView.setAnimation(R.raw.product_search)
            animationView.loop(true)
            animationView.playAnimation()

            binding.tvDataIsEmpty.text = "Mencari item..."

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val code = binding.etInputCodeProduct.text.toString()

            ProductApiService().getProductByCode( code, {

                beforeQty = it.qty.toInt()

                Picasso.get().load(it.image).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemProduct)
                binding.tvIdProduct.text = it._id
                binding.chipStatus.text = it.status
                binding.tvCodeItem.text = it.code_items
                binding.tvNameProduct.text = it.name
                binding.etQtyProduct.hint = it.qty
            }, {
                binding.etInputCodeProduct.text?.clear()
                binding.cardFullContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }, {
                binding.animationView.setAnimation(R.raw.product_not_found)
                animationView.loop(true)
                animationView.playAnimation()

                binding.animationView.visibility = View.VISIBLE
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.etInputCodeProduct.text?.clear()
                binding.tvDataIsEmpty.visibility = View.VISIBLE
                binding.cardFullContent.visibility = View.GONE
                binding.tvDataIsEmpty.text = "Data dengan code $code tidak di temukan"
            })

        }

        binding.btnSubmitStockIn.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val qtyInput = binding.etQtyProduct.text.toString()

            val resultCalculate = beforeQty + qtyInput.toInt()

            val qty = ProductRequest.RequestQtyOnly(resultCalculate.toString())


            ProductApiService().updateProductQty(this, binding.tvIdProduct.text.toString(), qty) { message, data ->


                val dataRequest = StockHistory.StockHistoryRequest(data.code_items, data.name, qtyInput, "IN")
                ProductApiService().createStockHistory( dataRequest)

                binding.cardFullContent.visibility = View.GONE
                binding.progressBar.visibility = View.GONE

                animationView.setAnimation(R.raw.successful)
                animationView.loop(false)
                animationView.playAnimation()

                binding.tvDataIsEmpty.text = "Quantity product telah ditambahkan sejumlah ${binding.etQtyProduct.text.toString()}"

                Handler().postDelayed( {
                    startActivity(Intent(this, MainActivity::class.java).setFlags(FLAG_ACTIVITY_CLEAR_TOP))
                }, 2000 )
            }

        }
    }
}