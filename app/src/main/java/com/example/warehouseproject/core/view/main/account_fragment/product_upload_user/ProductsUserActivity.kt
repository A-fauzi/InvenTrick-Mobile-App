package com.example.warehouseproject.core.view.main.account_fragment.product_upload_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductViewModel
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductViewModelFactory
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductsAdapterPaging
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductsLoadStateAdapter
import com.example.warehouseproject.databinding.ActivityProductsUserBinding
import io.paperdb.Paper
import kotlinx.coroutines.launch

class ProductsUserActivity : AppCompatActivity(), ProductsAdapterPaging.ProductsListenerPaging{
    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val FULLNAME = "fullname"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val STORAGE_PATH_PROFILE = "path"
        private const val PROFILE_PHOTO = "photo"
        private const val DIVISION = "division"
    }
    private lateinit var binding: ActivityProductsUserBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productsUserAdapterPaging: ProductsAdapterPaging

    private val uid = Paper.book().read<String>(ID).toString()
    private val token = Paper.book().read<String>(TOKEN).toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        setupViewModel()
        setupList()
        setupView()
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ApiService.create(token))
        )[ProductViewModel::class.java]
    }

   private fun setupList() {
       productsUserAdapterPaging = ProductsAdapterPaging(applicationContext, this)
       binding.rvProductsUser.apply {
           layoutManager = LinearLayoutManager(context)

           // with load adapter
//           adapter = productListAdapter.withLoadStateHeaderAndFooter(
//               header = ProductsLoadStateAdapter { productListAdapter.retry() },
//               footer = ProductsLoadStateAdapter { productListAdapter.retry() }
//           )
//            setHasFixedSize(true) --> Todo : jika ini di aktifkan, list tidak akan tampil saat initialisasi pertama kali (emang bangsat!)
            adapter = productsUserAdapterPaging
       }
   }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProduct.collect {
                productsUserAdapterPaging.submitData(it)
            }
        }
    }

    override fun onClickItem(data: Product?) {
        Toast.makeText(this, data?.user?.username, Toast.LENGTH_SHORT).show()
    }
}