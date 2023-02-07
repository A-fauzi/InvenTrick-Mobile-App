package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.room.db.ProductDB
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_histories.StockHistoriesActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), HomeAdapter.CallClickListener, HomeView {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var presenter: HomePresenter

    private lateinit var db: ProductDB



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        // Init db
        db = Room.databaseBuilder(requireActivity().applicationContext, ProductDB::class.java, "Warehouse").build()

        presenter = HomePresenter(this, HomeInteractor())
        setupRecyclerView()
        getData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnToAddProduct.setOnClickListener {
            startActivity(Intent(requireActivity(), AddProductActivity::class.java))
        }

        binding.cvStockIn.setOnClickListener {
            startActivity(Intent(requireActivity(), StockInActivity::class.java))
        }
        binding.cvStockOut.setOnClickListener {
            Toast.makeText(requireActivity(), "Fitur sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvStockHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), StockHistoriesActivity::class.java))
        }
        binding.cvProductCategory.setOnClickListener {
            Toast.makeText(requireActivity(), "Fitur sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }

        binding.rlTotalProduct.setBackgroundColor(Color.parseColor(RandomColor.generate()))
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(requireActivity(), arrayListOf(), this)
        binding.rvProduct.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }
    }

    private fun getData() {
        ProductApiService().getDataProduct(requireActivity(), { data, count ->

//            GlobalScope.launch {
//                val dao = db.productDao()
//                for (i in data) {
//                    dao.insertAll(
//                        Product(
//                            i._id,
//                            i.code_items,
//                            i.name,
//                            i.qty,
//                            i.category,
//                            i.sub_category,
//                            i.image,
//                            i.specification,
//                            i.price,
//                            i.location,
//                            i.status,
//                            i.model,
//                            i.lot,
//                            i.exp,
//                            i.created_at,
//                            i.updated_at))
//                }
//                val product: List<Product> = dao.getAll()
//                Log.d("HomeFragment", product.toString())
//            }
            showDataProduct(data)

            binding.tvCountProducts.text = context?.getString(R.string.product_count, count )
            if (count == "0") {
                binding.tvDataIsEmpty.visibility = View.VISIBLE
                binding.rvProduct.visibility = View.GONE
                binding.tvListProduct.visibility = View.GONE
            } else {
                binding.tvDataIsEmpty.visibility = View.GONE
                binding.rvProduct.visibility = View.VISIBLE
                binding.tvListProduct.visibility = View.VISIBLE
            }
        }, {
            binding.llFullContainer.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun showDataProduct(product: List<Product>) {
        homeAdapter.setData(product)
    }

    override fun onClickListenerDialog(data: Product) {
        presenter.showDetailDialog(requireActivity(), layoutInflater, data)
    }

    override fun moveMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

}