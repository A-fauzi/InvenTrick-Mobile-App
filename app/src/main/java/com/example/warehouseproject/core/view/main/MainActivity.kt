package com.example.warehouseproject.core.view.main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainAdapter.CallClickListener, MainView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainPresenter(this)

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
        mainAdapter = MainAdapter(this, arrayListOf(), this)
        binding.rvProduct.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            adapter = mainAdapter
        }
    }

    private fun getData() {
        ProductApiService().getDataProduct(this, {data, count ->
            showDataProduct(data)
            binding.tvCountProducts.text = resources.getString(R.string.product_count, count )
            if (count == "0") {
                binding.tvDataIsEmpty.visibility = View.VISIBLE
                binding.rvProduct.visibility = View.GONE
            } else {
                binding.tvDataIsEmpty.visibility = View.GONE
                binding.rvProduct.visibility = View.VISIBLE
            }
        }, {
            binding.llFullContainer.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun showDataProduct(product: List<Product>) {
        mainAdapter.setData(product)
    }

    override fun onClickListenerDialog(data: Product) {
        presenter.showDetailDialog(layoutInflater, this, data)
    }

    override fun moveMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}