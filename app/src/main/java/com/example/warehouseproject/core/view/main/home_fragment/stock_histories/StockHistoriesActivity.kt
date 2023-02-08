package com.example.warehouseproject.core.view.main.home_fragment.stock_histories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.warehouseproject.databinding.ActivityStockHistoriesBinding

class StockHistoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockHistoriesBinding

    private lateinit var presenter: StockHistoryPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockHistoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = StockHistoryPresenter(this)

        presenter.setupRecyclerView( binding.rvHistories)
        presenter.getData()

    }

}