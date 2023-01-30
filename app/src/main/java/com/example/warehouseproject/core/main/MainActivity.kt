package com.example.warehouseproject.core.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.warehouseproject.R
import com.example.warehouseproject.core.product.ModelProduct
import com.example.warehouseproject.core.product.add_product.AddProductActivity
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()

        setupRecyclerView()
        getData()
    }

    private fun setupRecyclerView() {
        mainAdapter = MainAdapter(arrayListOf())
        binding.rvProduct.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            adapter = mainAdapter
        }
    }

    private fun getData() {
        ProductApiService().getDataProduct(this, ) {data, count ->
            Log.d("MainActivity", "Data: $data")
            Log.d("MainActivity", "Count: $count")
            showDataProduct(data)
            binding.tvCountProducts.text = resources.getString(R.string.product_count, count )
        }
    }

    private fun showDataProduct(product: List<ModelProduct>) {
        mainAdapter.setData(product)
    }
}