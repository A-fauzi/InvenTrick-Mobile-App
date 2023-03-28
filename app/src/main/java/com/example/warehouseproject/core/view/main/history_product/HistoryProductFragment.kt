package com.example.warehouseproject.core.view.main.history_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.warehouseproject.databinding.FragmentStockHistoriesBinding

class HistoryProductFragment: Fragment() {
    private lateinit var binding: FragmentStockHistoriesBinding

    private lateinit var presenter: StockHistoryPresenter

    private fun initView(container: ViewGroup?) {
        binding = FragmentStockHistoriesBinding.inflate(layoutInflater, container, false)

        presenter = StockHistoryPresenter(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView(container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.setupRecyclerView( binding.rvHistories)
        presenter.getData()
    }
}