package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.DataBundle
import com.example.warehouseproject.core.view.main.detail_product.DetailProductActivity
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.product.add_product.steps.AddProductStepActivity
import com.example.warehouseproject.databinding.ActivityProductListAllBinding
import io.paperdb.Paper
import kotlinx.coroutines.launch

class ProductListAllActivity : AppCompatActivity(), ProductsAdapterPaging.ProductsListenerPaging,
    ProductApiService.OnFinishedGetProductByStatus {

    private lateinit var binding: ActivityProductListAllBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListAdapter: ProductsAdapterPaging

    private val token = Paper.book().read<String>("token").toString()

    private fun initView() {
        Paper.init(this)
        binding.btnAddProduct.btnComponent.text = getString(R.string.add_product)
        binding.btnAddProduct.btnComponent.isEnabled = true
        productListAdapter = ProductsAdapterPaging(applicationContext, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
        setupList()
        setupView()

        ProductApiService(token).getProductByStatus("active", { data ->
            binding.tvCountActive.text = data.totalCount.toString()
            binding.tvCountActive.textSize = 24f
        }, this)

        ProductApiService(token).getProductByStatus("in-progress", { data ->
            binding.tvCountOnProgress.text = data.totalCount.toString()
            binding.tvCountOnProgress.textSize = 24f
        }, this)
    }

    override fun onStart() {
        super.onStart()

        binding.btnAddProduct.btnComponent.setOnClickListener {
            startActivity(Intent(this, AddProductStepActivity::class.java))
        }
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProduct.collect {
                productListAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {

        binding.rvListProductAll.apply {
            layoutManager = LinearLayoutManager(context)

            // with load adapter
            adapter = productListAdapter.withLoadStateHeaderAndFooter(
                header = ProductsLoadStateAdapter { productListAdapter.retry() },
                footer = ProductsLoadStateAdapter { productListAdapter.retry() }
            )

            productListAdapter.addLoadStateListener { loadState ->
                binding.progressBarListProduct.visibility = View.VISIBLE

                if (loadState.append.endOfPaginationReached) {
                    if (productListAdapter.itemCount < 1) {
                        // data empty state
                        binding.progressBarListProduct.visibility = View.GONE
                        // Set text in bellow
                    } else {
                        // data is not empty
                        binding.progressBarListProduct.visibility = View.GONE
                        val dataCount = productListAdapter.itemCount.toString()
                        binding.tvCountAllProduct.text = dataCount
                        binding.tvCountAllProduct.textSize = 24f
                    }
                }
            }
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
            val bundle = DataBundle.putProductData(data)
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onErrorGetProductByStatus(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureResponseGetProductByStatus(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}