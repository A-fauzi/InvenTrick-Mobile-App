package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.PreferenceHelper
import com.example.warehouseproject.core.helper.PreferenceHelper.loadData
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.helper.SavedPreferenceUser
import com.example.warehouseproject.core.helper.SimpleDateFormat
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.DataFromAssets
import com.example.warehouseproject.core.utils.DataUser.EMAIL
import com.example.warehouseproject.core.utils.DataUser.PHOTO_URI
import com.example.warehouseproject.core.utils.DataUser.USERNAME
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.category.ProductCategoryActivity
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog
import com.example.warehouseproject.core.view.main.home_fragment.stock_histories.StockHistoriesActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import java.lang.Math.abs

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
            binding.newTxtTopbar.viewEnd.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.animation_button))
            popUpMenu()
        }

        shimmerViewContainer.startShimmer()
        shimmerViewTotalProduct.startShimmer()

        binding.btnToAddProduct.setOnClickListener {
            binding.btnToAddProduct.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.animation_button))
            startActivity(Intent(requireActivity(), AddProductActivity::class.java))
        }

        binding.rlTotalProduct.setBackgroundColor(Color.parseColor(RandomColor.generate()))

        val photo = SavedPreferenceUser.getPhoto(requireActivity())
        val name = SavedPreferenceUser.getUsername(requireActivity())
        val email = SavedPreferenceUser.getEmail(requireActivity())

        Picasso.get().load(photo.toString()).placeholder(R.drawable.example_user).error(R.drawable.img_example).into(binding.newTxtTopbar.viewStart)
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
                    startActivity(Intent(requireActivity(), ProductCategoryActivity::class.java))
                }
            }

            true
        }
        popupMenu.show()
    }

}