package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.databinding.ActivityProductListAllBinding
import io.paperdb.Paper
import kotlinx.coroutines.launch

class ProductListAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListAllBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListAdapter: ProductsAdapterPaging

    private val token = Paper.book().read<String>("token").toString()

    private fun initView() {
        Paper.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
        setupList()
        setupView()
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProduct.collect {
                productListAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {
        productListAdapter = ProductsAdapterPaging(applicationContext)
        binding.rvListProductAll.apply {
            layoutManager = LinearLayoutManager(context)

            // with load adapter
            adapter = productListAdapter.withLoadStateHeaderAndFooter(
                header = ProductsLoadStateAdapter { productListAdapter.retry() },
                footer = ProductsLoadStateAdapter { productListAdapter.retry() }
            )
//            setHasFixedSize(true) --> Todo : jika ini di aktifkan, list tidak akan tampil saat initialisasi pertama kali (emang bangsat!)
//            adapter = productListAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ApiService.create(token))
        )[ProductViewModel::class.java]
    }
}