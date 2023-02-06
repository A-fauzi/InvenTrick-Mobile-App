package com.example.warehouseproject.core.view.main.home_fragment.stock_in_product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivityStockInBinding
import com.squareup.picasso.Picasso

class StockInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnSearchProduct.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            binding.tvDataIsEmpty.visibility = View.GONE

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val code = binding.etInputCodeProduct.text.toString()

            ProductApiService().getProductByCode(this, code, {
                Picasso.get().load(it.image).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemProduct)
                binding.tvIdProduct.text = it._id
                binding.chipStatus.text = it.status
                binding.tvCodeItem.text = it.code_items
                binding.tvNameProduct.text = it.name
                binding.etQtyProduct.setText(it.qty)
            }, {
                binding.etInputCodeProduct.text?.clear()
                binding.cardFullContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            })

        }

        binding.btnSubmitStockIn.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

            val qty = ProductRequest.RequestQtyOnly(binding.etQtyProduct.text.toString())

            ProductApiService().updateProductQty(this, binding.tvIdProduct.text.toString(), qty) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }
    }
}