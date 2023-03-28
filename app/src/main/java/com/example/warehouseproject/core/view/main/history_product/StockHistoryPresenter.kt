package com.example.warehouseproject.core.view.main.history_product

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import io.paperdb.Paper

class StockHistoryPresenter(private val context: Context): StockHistoriesAdapter.Listener {
    private lateinit var stockHistoriesAdapter: StockHistoriesAdapter

    fun setupRecyclerView(recyclerView: RecyclerView) {
        stockHistoriesAdapter = StockHistoriesAdapter(context, arrayListOf(), this)
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = stockHistoriesAdapter
        }
    }

    private fun showDataProduct(product: List<StockHistory>) {
        stockHistoriesAdapter.setData(product)
    }

    fun getData() {
        Paper.init(context)
        val token = Paper.book().read<String>("token").toString()
        ProductApiService(token).getStockHistories { msg, data, count ->
            showDataProduct(data.reversed())
        }
    }

    override fun onClickItemHistory(data: StockHistory) {
        Paper.init(context)
        val token = Paper.book().read<String>("token").toString()
        ProductApiService(token).getProductByCode(data.code_items, {}, {
            Toast.makeText(context, "Product masih tersedia", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(context, "Product sudah tidak tersedia", Toast.LENGTH_SHORT).show()
        })
    }
}