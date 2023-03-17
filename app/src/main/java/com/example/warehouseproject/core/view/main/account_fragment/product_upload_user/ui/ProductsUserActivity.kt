package com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
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
    private lateinit var viewModel: ProductsUserViewModel
    private lateinit var productsAdapterPaging: ProductsAdapterPaging

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
            ProductsUserViewModelFactory(ProductsUserApiService.create(token))
        )[ProductsUserViewModel::class.java]
    }

   private fun setupList() {
       productsAdapterPaging = ProductsAdapterPaging(applicationContext, this)
       binding.rvProductsUser.apply {
           layoutManager = LinearLayoutManager(context)

           // with load adapter
           adapter = productsAdapterPaging.withLoadStateHeaderAndFooter(
               header = ProductsLoadStateAdapter { productsAdapterPaging.retry() },
               footer = ProductsLoadStateAdapter { productsAdapterPaging.retry() }
           )

           // Untuk mengecek data count pada paging first
           productsAdapterPaging.addLoadStateListener { loadState ->
               if (loadState.append.endOfPaginationReached) {
                   if (productsAdapterPaging.itemCount < 1) {
                       Toast.makeText(this@ProductsUserActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
                   } else {
                       Toast.makeText(this@ProductsUserActivity, "Data Ada ${productsAdapterPaging.itemCount}", Toast.LENGTH_SHORT).show()
                   }
               }
           }

//            setHasFixedSize(true) --> Todo : jika ini di aktifkan, list tidak akan tampil saat initialisasi pertama kali (emang bangsat!)
//            adapter = productsAdapterPaging
       }
   }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProductsUser.collect {
                productsAdapterPaging.submitData(it)
            }
        }
    }

    override fun onClickItem(data: Product?) {
        Toast.makeText(this, data?.user?.username, Toast.LENGTH_SHORT).show()
    }
}