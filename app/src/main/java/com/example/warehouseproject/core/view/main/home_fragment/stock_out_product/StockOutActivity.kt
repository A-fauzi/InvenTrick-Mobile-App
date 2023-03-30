package com.example.warehouseproject.core.view.main.home_fragment.stock_out_product

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.utils.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityStockOutBinding
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class StockOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockOutBinding

    private lateinit var animationView: LottieAnimationView

    private var beforeQty: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        animationView = binding.stockOut.animationView
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        binding.stockOut.tvProductMasuk.text = "Product Keluar"
        binding.stockOut.outlinedTextFieldQtyProduct.boxStrokeColor = resources.getColor(R.color.red_smooth)
        binding.stockOut.btnSubmitStockIn.setBackgroundColor(resources.getColor(R.color.red_smooth))
        binding.stockOut.outlinedTextFieldQtyProduct.helperText = "Jumlah quantity yang keluar"

        animationView.setAnimation(R.raw.product)
        animationView.loop(false)
        animationView.playAnimation()

        val codeItemBundle = intent.extras?.getString("code_items_key")
        binding.stockOut.etInputCodeProduct.setText(codeItemBundle)

        binding.stockOut.btnSearchProduct.setOnClickListener {

            binding.stockOut.animationView.setAnimation(R.raw.product_search)
            animationView.loop(true)
            animationView.playAnimation()

            binding.stockOut.tvDataIsEmpty.text = "Mencari item..."

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val code = binding.stockOut.etInputCodeProduct.text.toString()


            val token = Paper.book().read<String>("token").toString()
            ProductApiService(token).getProductByCode( code, {

                beforeQty = it.qty.toInt()

                Picasso.get().load(it.image).centerCrop().resize(500, 500).error(R.drawable.img_example).into(binding.stockOut.ivItemProduct)
                binding.stockOut.tvIdProduct.text = it._id
                binding.stockOut.chipStatus.text = it.status
                binding.stockOut.tvCodeItem.text = it.code_items
                binding.stockOut.tvNameProduct.text = it.name
                binding.stockOut.etQtyProduct.hint = it.qty
            }, {
                binding.stockOut.etInputCodeProduct.text?.clear()
                binding.stockOut.cardFullContent.visibility = View.VISIBLE
                binding.stockOut.progressBar.visibility = View.GONE

                binding.stockOut.containerSearchProductView.visibility = View.GONE
            }, {
                binding.stockOut.animationView.setAnimation(R.raw.product_not_found)
                animationView.loop(true)
                animationView.playAnimation()

                binding.stockOut.animationView.visibility = View.VISIBLE
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                binding.stockOut.progressBar.visibility = View.GONE
                binding.stockOut.etInputCodeProduct.text?.clear()
                binding.stockOut.tvDataIsEmpty.visibility = View.VISIBLE
                binding.stockOut.cardFullContent.visibility = View.GONE
                binding.stockOut.tvDataIsEmpty.text = "Data dengan code $code tidak di temukan"
            })

        }

        binding.stockOut.btnSubmitStockIn.setOnClickListener {

            binding.stockOut.progressBar.visibility = View.VISIBLE

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val qtyInput = binding.stockOut.etQtyProduct.text.toString()

            if (binding.stockOut.etQtyProduct.text.toString().isEmpty()) {
                binding.stockOut.progressBar.visibility = View.GONE
                binding.stockOut.outlinedTextFieldQtyProduct.helperText = "is required!"
                binding.stockOut.outlinedTextFieldQtyProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
            } else if ( beforeQty < qtyInput.toInt()) {
                binding.stockOut.progressBar.visibility = View.GONE
                binding.stockOut.outlinedTextFieldQtyProduct.helperText = "Jumlah quantity product tidak memenuhi, jumlah saat ini $beforeQty "
                binding.stockOut.outlinedTextFieldQtyProduct.isHelperTextEnabled = true
                binding.stockOut.outlinedTextFieldQtyProduct.setHelperTextColor(getColorStateList(R.color.red_smooth))
            } else {

                val resultCalculate = beforeQty - qtyInput.toInt()

                val qty = ProductRequest.RequestQtyOnly(resultCalculate.toString())


                val token = Paper.book().read<String>("token").toString()
                val currentUid = Paper.book().read<String>(Constant.User.ID).toString()
                ProductApiService(token).updateProductQty(this, binding.stockOut.tvIdProduct.text.toString(), qty,  { message, data ->


                    val dataRequest = StockHistory.StockHistoryRequest(data.code_items, data.name, qtyInput, "OUT", currentUid)
                    ProductApiService(token).createStockHistory( dataRequest)

                    binding.stockOut.containerSearchView.visibility = View.GONE
                    binding.stockOut.tvDescInputCode.visibility = View.GONE
                    binding.stockOut.cardFullContent.visibility = View.GONE
                    binding.stockOut.progressBar.visibility = View.GONE

                    animationView.setAnimation(R.raw.successful)
                    animationView.loop(false)
                    animationView.playAnimation()

                    binding.stockOut.tvDataIsEmpty.text = "Quantity product telah keluar sejumlah ${binding.stockOut.etQtyProduct.text.toString()}"

                    Handler().postDelayed( {
                        startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }, 2000 )
                }, {})


            }

        }
    }
}