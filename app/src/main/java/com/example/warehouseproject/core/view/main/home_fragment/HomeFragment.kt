package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.helper.SavedPreferenceUser
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.home_fragment.category.ProductCategoryActivity
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog
import com.example.warehouseproject.core.view.main.home_fragment.stock_histories.StockHistoriesActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import io.paperdb.Paper

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

        shimmerViewContainer = binding.shimmerViewContainerListProduct
        shimmerViewTotalProduct = binding.shimmerViewTotalProduct

        presenter = HomePresenter(this, DetailDialog())

        Paper.init(requireContext())

        setupRecyclerView()

        getData()

        val status = Paper.book().read<String>("status").toString()
        when(status) {
            "online" -> {
                binding.newTxtTopbar.cvStatusActivityUser.setCardBackgroundColor(Color.GREEN)
                binding.newTxtTopbar.tvStatusActivityUser.text = status
            }
            "offline" -> {
                binding.newTxtTopbar.cvStatusActivityUser.setCardBackgroundColor(Color.RED)
                binding.newTxtTopbar.tvStatusActivityUser.text = status
            }
        }

//        // paperdb
//        Paper.init(requireContext())
//
//        val list = Paper.book().read<List<Product>>("products")
//        if (list != null) {
//            val count = Paper.book().read<String>("count")
//            showDataProduct(list)
//            binding.tvCountProducts.text = count.toString()
//            binding.tvCountProducts.visibility = View.VISIBLE
//            if (count == "0") {
//                shimmerViewContainer.visibility = View.GONE
//                binding.tvDataIsEmpty.visibility = View.VISIBLE
//                binding.rvProduct.visibility = View.GONE
//                binding.tvListProduct.visibility = View.GONE
//            } else {
//                shimmerViewContainer.visibility = View.GONE
//                binding.tvDataIsEmpty.visibility = View.GONE
//                binding.rvProduct.visibility = View.VISIBLE
//                binding.tvListProduct.visibility = View.VISIBLE
//                shimmerViewTotalProduct.visibility = View.GONE
//            }
//        } else {
//            getData()
//        }

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

//        Picasso.get().load(photo.toString()).placeholder(R.drawable.example_user).error(R.drawable.img_example).into(binding.newTxtTopbar.viewStart)
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
        val token = Paper.book().read<String>("token").toString()
        ProductApiService(token).getDataProduct(requireActivity(), { data, count ->

            // save data
//            val product: List<Product> = data
//            val productCount: String = count
//            Paper.book().write("products", product)
//            Paper.book().write("count", productCount)

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
        },{
            binding.llFullContainer.visibility = View.VISIBLE
            binding.tvCountProducts.visibility = View.VISIBLE
            shimmerViewTotalProduct.visibility = View.GONE
        }) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            if (it == "user is deleted and token not valid") {
                Toast.makeText(requireContext(), "your is blocked", Toast.LENGTH_SHORT).show()
                Paper.book().destroy()
                val intent = Intent(requireContext(), SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finish()
            }
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