package com.example.warehouseproject.core.view.main.home_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.category.ProductCategoryActivity
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductListAllActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_histories.StockHistoriesActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import io.paperdb.Paper

class HomeFragment : Fragment(), ProductListAdapter.CallClickListener, HomeView {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var presenter: HomePresenter
    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var shimmerViewTotalProduct: ShimmerFrameLayout


    private val token = Paper.book().read<String>("token").toString()

    private fun initView(container: ViewGroup?) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        shimmerViewContainer = binding.shimmerViewContainerListProduct
        shimmerViewTotalProduct = binding.shimmerViewTotalProduct
        presenter = HomePresenter(this, DetailDialog(), ProductApiService(token))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initView(container)

        presenter.getDataProducts(1)

        Paper.init(requireContext())

        setupRecyclerView()

        userStatusNetwork()

        return binding.root
    }

    private fun setupRecyclerView() {
        productListAdapter = ProductListAdapter(requireContext(), arrayListOf(), this)
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = productListAdapter
            setHasFixedSize(true)
        }
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
                    startActivity(Intent(requireActivity(), ProductCategoryActivity::class.java))
                }
            }

            true
        }
        popupMenu.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.tvListMoreAll.setOnClickListener {
            startActivity(Intent(activity, ProductListAllActivity::class.java))
        }

        setUpTopBar()
        startShimmerView()
        binding.btnToAddProduct.setOnClickListener {
            binding.btnToAddProduct.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.animation_button))
            startActivity(Intent(requireActivity(), AddProductActivity::class.java))
        }
        binding.rlTotalProduct.setBackgroundColor(Color.parseColor(RandomColor.generate()))

    }

    private fun userStatusNetwork() {
        when (val status = Paper.book().read<String>("status").toString()) {
            "online" -> {
                binding.newTxtTopbar.cvStatusActivityUser.setCardBackgroundColor(Color.GREEN)
                binding.newTxtTopbar.tvStatusActivityUser.text = status
            }
            "offline" -> {
                binding.newTxtTopbar.cvStatusActivityUser.setCardBackgroundColor(Color.RED)
                binding.newTxtTopbar.tvStatusActivityUser.text = status
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTopBar() {
        binding.newTxtTopbar.txtTopBar.text = "Home"
        binding.newTxtTopbar.viewEnd.setOnClickListener {
            binding.newTxtTopbar.viewEnd.startAnimation(
                AnimationUtils.loadAnimation(
                    requireActivity(),
                    R.anim.animation_button
                )
            )
            popUpMenu()
        }
    }

    private fun startShimmerView() {
        shimmerViewContainer.startShimmer()
        shimmerViewTotalProduct.startShimmer()
    }

    private fun showDataProduct(product: List<Product>) {
        productListAdapter.setData(product)
    }

    override fun onClickListenerDialog(data: Product) {
        presenter.showDetailDialog(requireActivity(), layoutInflater, data)
    }

    override fun moveMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    @SuppressLint("SetTextI18n")
    override fun successResponseBodyGetProductsView(data: List<Product>, productResponses: ProductResponses) {
        showDataProduct(data)

        binding.llFullContainer.visibility = View.VISIBLE
        binding.tvCountProducts.visibility = View.VISIBLE
        shimmerViewTotalProduct.visibility = View.GONE

        Log.d("PAGE", "page ke ${productResponses.currentPage}/${productResponses.totalPages}")
        binding.tvCountProducts.text = productResponses.totalCount.toString()
        if (productResponses.totalCount == 0) {
            shimmerViewContainer.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.tvDataIsEmpty.visibility = View.VISIBLE
        } else {
            shimmerViewContainer.visibility = View.GONE
            binding.rvProduct.visibility = View.VISIBLE
            shimmerViewTotalProduct.visibility = View.GONE
            binding.tvDataIsEmpty.visibility = View.GONE
        }
    }

    override fun errorResponseBodyGetProductsView(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        if (msg == "Unauthorized!") {
            Paper.book().destroy()
            clearSessionOrSignOut()
        }else if (msg == "user is deleted and token not valid") {
            Toast.makeText(requireContext(), "your is blocked", Toast.LENGTH_SHORT).show()
            Paper.book().destroy()
            clearSessionOrSignOut()
        }
    }

    private fun clearSessionOrSignOut() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onFailureRequestGetProductsView(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}