package com.example.warehouseproject.core.view.main.home_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
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
import com.squareup.picasso.Picasso
import io.paperdb.Paper


class HomeFragment : Fragment(), ProductListAdapter.CallClickListener, HomeView {
    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val FULLNAME = "fullname"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val STORAGE_PATH_PROFILE = "path"
        private const val PROFILE_PHOTO = "photo"
    }

    private var activity: Activity? = null

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var presenter: HomePresenter
    private lateinit var shimmerViewContainer: ShimmerFrameLayout


    private val token = Paper.book().read<String>(TOKEN).toString()
    private val profileImg = Paper.book().read<String>(PROFILE_PHOTO).toString()

    private fun initView(container: ViewGroup?) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        shimmerViewContainer = binding.shimmerViewContainerListProduct
        presenter = HomePresenter(this, DetailDialog(), ProductApiService(token))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initView(container)

        presenter.getDataProducts(1)

        Paper.init(requireContext().applicationContext)

        setupRecyclerView()

        userStatusNetwork()

        return binding.root
    }

    private fun setupRecyclerView() {
        productListAdapter =
            ProductListAdapter(requireContext().applicationContext, arrayListOf(), this)
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = productListAdapter
            setHasFixedSize(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("RestrictedApi")
    private fun popUpMenu() {
        val popupMenu = PopupMenu(
            activity,
            binding.newTxtTopbar.viewEnd,
            Gravity.END,
            0,
            R.style.myListPopupWindow
        )
        popupMenu.menuInflater.inflate(R.menu.product_activity_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.history -> {
                    startActivity(Intent(activity, StockHistoriesActivity::class.java))
                }
                R.id.category -> {
                    startActivity(Intent(activity, ProductCategoryActivity::class.java))
                }
            }

            true
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.tvListMoreAll.setOnClickListener {
            startActivity(Intent(activity, ProductListAllActivity::class.java))
        }

        setUpTopBar()
        startShimmerView()

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

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun setUpTopBar() {
        Picasso.get().load(profileImg).error(R.drawable.example_user)
            .into(binding.newTxtTopbar.viewStart)
        binding.newTxtTopbar.txtTopBar.text = "Home"
        binding.newTxtTopbar.viewEnd.setOnClickListener {
            binding.newTxtTopbar.viewEnd.startAnimation(
                AnimationUtils.loadAnimation(
                    activity,
                    R.anim.animation_button
                )
            )
            popUpMenu()
        }
    }

    private fun startShimmerView() {
        shimmerViewContainer.startShimmer()
    }

    private fun showDataProduct(product: List<Product>) {
        productListAdapter.setData(product)
    }

    override fun onClickListenerDialog(data: Product) {
        activity?.let { presenter.showDetailDialog(it, layoutInflater, data) }
    }

    override fun moveMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    @SuppressLint("SetTextI18n")
    override fun successResponseBodyGetProductsView(
        data: List<Product>,
        productResponses: ProductResponses
    ) {
        showDataProduct(data)

        binding.contentContainer.visibility = View.VISIBLE
        binding.viewErrorResponse.root.visibility = View.GONE

        if (productResponses.totalCount == 0) {
            binding.contentContainer.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.containerViewDataEmpty.visibility = View.VISIBLE
            binding.btnAddProductNow.setOnClickListener {
                startActivity(Intent(requireActivity(), AddProductActivity::class.java))
            }
        } else {
            binding.contentContainer.visibility = View.VISIBLE
            shimmerViewContainer.visibility = View.GONE
            binding.rvProduct.visibility = View.VISIBLE
            binding.containerViewDataEmpty.visibility = View.GONE
        }
    }

    override fun errorResponseBodyGetProductsView(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        if (msg == "Unauthorized!") {
            Paper.book().destroy()
            clearSessionOrSignOut()
        } else if (msg == "user is deleted and token not valid") {
            Toast.makeText(activity, "your is blocked", Toast.LENGTH_SHORT).show()
            Paper.book().destroy()
            clearSessionOrSignOut()
        } else {
            Toast.makeText(activity, "Error Respon: $msg", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearSessionOrSignOut() {
        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onFailureRequestGetProductsView(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        binding.contentContainer.visibility = View.GONE
        binding.viewErrorResponse.root.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = if (context is Activity) context else null
    }

}