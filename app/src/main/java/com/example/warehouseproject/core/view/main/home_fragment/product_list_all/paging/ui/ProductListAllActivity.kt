package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.utils.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.home_fragment.HomePresenter
import com.example.warehouseproject.core.view.main.home_fragment.HomeView
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.ActivityProductListAllBinding
import io.paperdb.Paper
import kotlinx.coroutines.launch

class ProductListAllActivity : AppCompatActivity(), ProductsAdapterPaging.ProductsListenerPaging, HomeView {

    private lateinit var binding: ActivityProductListAllBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListAdapter: ProductsAdapterPaging

    private lateinit var presenter: HomePresenter

    private val token = Paper.book().read<String>("token").toString()

    private fun initView() {
        Paper.init(this)
        presenter = HomePresenter(this, DetailDialog(), ProductApiService(token))
        binding.shimmerViewTotalProduct.startShimmer()

        binding.btnToAddProduct.setOnClickListener {
            binding.btnToAddProduct.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_button))
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        binding.rlTotalProduct.setBackgroundColor(Color.parseColor(RandomColor.generate()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
        setupList()
        setupView()

        presenter.getDataProducts()
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProduct.collect {
                productListAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {
        productListAdapter = ProductsAdapterPaging(applicationContext, this)
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

    override fun onClickItem(data: Product?) {
        if (data != null) {
            presenter.showDetailDialog(this, layoutInflater, data)
        }
    }

    override fun moveMainActivity() {

    }

    override fun successResponseBodyGetProductsView(
        data: List<Product>,
        productResponses: ProductResponses
    ) {
        binding.shimmerViewTotalProduct.visibility = View.GONE
        binding.tvCountProducts.visibility = View.VISIBLE
        binding.tvCountProducts.text = productResponses.totalCount.toString()
    }

    override fun errorResponseBodyGetProductsView(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureRequestGetProductsView(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}