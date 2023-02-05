package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.example.warehouseproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), HomeAdapter.CallClickListener, HomeView {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var presenter: HomePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        presenter = HomePresenter(this)
        setupRecyclerView()
        getData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnToAddProduct.setOnClickListener {
            startActivity(Intent(requireActivity(), AddProductActivity::class.java))
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
            binding.tvCountProducts.text = resources.getString(R.string.product_count, count )
            if (count == "0") {
                binding.tvDataIsEmpty.visibility = View.VISIBLE
                binding.rvProduct.visibility = View.GONE
            } else {
                binding.tvDataIsEmpty.visibility = View.GONE
                binding.rvProduct.visibility = View.VISIBLE
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
        presenter.showDetailDialog(layoutInflater, requireActivity(), data)
    }

    override fun moveMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

}