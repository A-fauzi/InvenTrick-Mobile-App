package com.example.warehouseproject.core.view.main.home_fragment.stock_histories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.home_fragment.HomeAdapter
import com.example.warehouseproject.databinding.ActivityStockHistoriesBinding

class StockHistoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockHistoriesBinding
    private lateinit var stockHistoriesAdapter: StockHistoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockHistoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        getData()

    }

    private fun setupRecyclerView() {
        stockHistoriesAdapter = StockHistoriesAdapter(this, arrayListOf())
        binding.rvHistories.apply {
            layoutManager =
                LinearLayoutManager(this@StockHistoriesActivity, LinearLayoutManager.VERTICAL, false)
            adapter = stockHistoriesAdapter
        }
    }

    private fun showDataProduct(product: List<StockHistory>) {
        stockHistoriesAdapter.setData(product)
    }

    private fun getData() {
        ProductApiService().getStockHistories {msg, data, count ->
            Log.d("StockHistoriesActivity", msg)
            Log.d("StockHistoriesActivity", count)
            showDataProduct(data)
        }
    }
}