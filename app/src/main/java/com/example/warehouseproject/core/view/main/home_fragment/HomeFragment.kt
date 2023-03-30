package com.example.warehouseproject.core.view.main.home_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.User.PROFILE_PHOTO
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.ProductResponses
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.DataBundle
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.detail_product.DetailProductActivity
import com.example.warehouseproject.core.view.main.home_fragment.category.ProductCategoryActivity
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductListAllActivity
import com.example.warehouseproject.core.view.main.home_fragment.webview.NewsActivity
import com.example.warehouseproject.core.view.product.add_product.steps.AddProductStepActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import io.paperdb.Paper


class HomeFragment : Fragment(), ProductListAdapter.CallClickListener, HomeView {
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
        presenter = HomePresenter(this, ProductApiService(token))
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
                R.id.category -> {
                    startActivity(Intent(activity, ProductCategoryActivity::class.java))
                }
                R.id.webView -> {
                    startActivity(Intent(requireActivity(), NewsActivity::class.java))
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

    override fun onClickListenerProduct(data: Product) {
        val bundle = DataBundle.putProductData(data)
        val intent = Intent(requireActivity(), DetailProductActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
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
                startActivity(Intent(requireActivity(), AddProductStepActivity::class.java))
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
        when (msg) {
            "Unauthorized!" -> {
                Paper.book().destroy()
                clearSessionOrSignOut()
            }
            "user is deleted and token not valid" -> {
                Toast.makeText(activity, "your is blocked", Toast.LENGTH_SHORT).show()
                Paper.book().destroy()
                clearSessionOrSignOut()
            }
            else -> {
                Toast.makeText(activity, "Wahh ada error nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearSessionOrSignOut() {
        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onFailureRequestGetProductsView(msg: String) {
        binding.contentContainer.visibility = View.GONE
        binding.viewErrorResponse.root.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = if (context is Activity) context else null
    }

}