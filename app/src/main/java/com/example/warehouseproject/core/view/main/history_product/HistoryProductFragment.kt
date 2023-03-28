package com.example.warehouseproject.core.view.main.history_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.databinding.FragmentStockHistoriesBinding
import io.paperdb.Paper
import kotlinx.coroutines.launch

class HistoryProductFragment: Fragment(),  StockHistoryAdapterPaging.StockHistoriesListener{
    private lateinit var binding: FragmentStockHistoriesBinding
    private lateinit var viewModel: StockHistoryViewModel
    private lateinit var stockHistoryAdapter: StockHistoryAdapterPaging

    private fun initView(container: ViewGroup?) {
        Paper.init(requireActivity())
        binding = FragmentStockHistoriesBinding.inflate(layoutInflater, container, false)
        stockHistoryAdapter = StockHistoryAdapterPaging(requireActivity(), this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView(container)
        setupViewModel()
        setupList()
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listStockHistory.collect {
                stockHistoryAdapter.submitData(it)
            }
        }
    }
    private fun setupList() {

        binding.rvHistories.apply {
            layoutManager = LinearLayoutManager(context)

            // with load adapter
//            adapter = stockHistoryAdapter.withLoadStateHeaderAndFooter(
//                header = ProductsLoadStateAdapter { productListAdapter.retry() },
//                footer = ProductsLoadStateAdapter { productListAdapter.retry() }
//            )
            adapter = stockHistoryAdapter

            stockHistoryAdapter.addLoadStateListener { loadState ->

                if (loadState.append.endOfPaginationReached) {
                    if (stockHistoryAdapter.itemCount < 1) {
                        // data empty state
                        // Set text in bellow
                    } else {
                        // data is not empty
                        val dataCount = stockHistoryAdapter.itemCount.toString()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        val token = Paper.book().read<String>(Constant.User.TOKEN).toString()
        viewModel = ViewModelProvider(
            this,
            StockHistoryViewModelFactory(ApiService.create(token))
        )[StockHistoryViewModel::class.java]
    }

    override fun onClickItemHistory(data: StockHistory) {

    }
}