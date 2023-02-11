package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.PreferenceHelper
import com.example.warehouseproject.core.helper.PreferenceHelper.loadData
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.helper.SimpleDateFormat
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog
import com.example.warehouseproject.core.view.main.home_fragment.stock_histories.StockHistoriesActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout

class HomeFragment : Fragment(), HomeAdapter.CallClickListener, HomeView {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var presenter: HomePresenter

    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var shimmerViewTotalProduct: ShimmerFrameLayout




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        activity?.setActionBar(binding.toolbar)

        presenter = HomePresenter(this, DetailDialog())
        setupRecyclerView()
        getData()

        shimmerViewContainer = binding.shimmerViewContainerListProduct
        shimmerViewTotalProduct = binding.shimmerViewTotalProduct

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.newTxtTopbar.txtTopBar.text = "Home"
        binding.newTxtTopbar.viewEnd.setOnClickListener {
            popUpMenu()
        }

        shimmerViewContainer.startShimmer()
        shimmerViewTotalProduct.startShimmer()

        binding.btnToAddProduct.setOnClickListener {
            startActivity(Intent(requireActivity(), AddProductActivity::class.java))
            activity?.finish()
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

            showDataProduct(data)

            binding.tvCountProducts.text = count
            if (count == "0") {
                shimmerViewContainer.visibility = View.GONE
                binding.tvDataIsEmpty.visibility = View.VISIBLE
                binding.rvProduct.visibility = View.GONE
                binding.tvListProduct.visibility = View.GONE
            } else {
                shimmerViewContainer.visibility = View.GONE
                binding.tvDataIsEmpty.visibility = View.GONE
                binding.rvProduct.visibility = View.VISIBLE
                binding.tvListProduct.visibility = View.VISIBLE
                shimmerViewTotalProduct.visibility = View.GONE
            }
        }) {
            binding.llFullContainer.visibility = View.VISIBLE
            binding.tvCountProducts.visibility = View.VISIBLE
            shimmerViewTotalProduct.visibility = View.GONE
        }
    }

    private fun showDataProduct(product: List<Product>) {
        homeAdapter.setData(product)
    }

    override fun onClickListenerDialog(data: Product) {
        presenter.showDetailDialog(requireActivity(), layoutInflater, data)
    }

    override fun moveMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    private fun popUpMenu() {
        val popupMenu = PopupMenu(activity, binding.newTxtTopbar.viewEnd, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.product_activity_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.stockIn -> {
                    startActivity(Intent(requireActivity(), StockInActivity::class.java))
                }
                R.id.stockOut -> {
                    startActivity(Intent(requireActivity(), StockOutActivity::class.java))
                }
                R.id.history -> {
                    startActivity(Intent(requireActivity(), StockHistoriesActivity::class.java))
                }
                R.id.category -> {
                    Toast.makeText(requireActivity(), "Fitur sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
        popupMenu.show()
    }

}