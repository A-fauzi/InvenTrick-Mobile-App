package com.example.warehouseproject.core.view.main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.view.product.ModelProduct
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), MainAdapter.CallClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        binding.rlTotalProduct.setBackgroundColor(Color.parseColor(RandomColor.generate()))

    }

    override fun onStart() {
        super.onStart()

        setupRecyclerView()
        getData()
    }

    private fun setupRecyclerView() {
        mainAdapter = MainAdapter(arrayListOf(), this)
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

    override fun onClickListener(data: ModelProduct) {

        val binding = ItemDetailDialogBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(this)

        Picasso.get().load(data.image).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemDetail)
        binding.statusDetail.text = data.status
        binding.chipCodeItemDetail.text = data.code_items
        binding.tvNameProductDetail.text = data.name


        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}