package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.DataBundle
import com.example.warehouseproject.core.view.main.detail_product.DetailProductActivity
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.product.add_product.steps.AddProductStepActivity
import com.example.warehouseproject.databinding.ActivityProductListAllBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import io.paperdb.Paper
import kotlinx.coroutines.launch

class ProductListAllActivity : AppCompatActivity(), ProductsAdapterPaging.ProductsListenerPaging,
    ProductApiService.OnFinishedGetProductByStatus {

    private lateinit var binding: ActivityProductListAllBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListAdapter: ProductsAdapterPaging
    private lateinit var pieChart: PieChart

    private val token = Paper.book().read<String>("token").toString()

    private fun initView() {
        Paper.init(this)
        binding.btnAddProduct.btnComponent.text = getString(R.string.add_product)
        productListAdapter = ProductsAdapterPaging(applicationContext, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
        setupList()
        setupView()

        getProductByStatus("in-progress", binding.tvCountOnProgress) {
            binding.btnAddProduct.btnComponent.isEnabled = true

            dataChart()
        }
        getProductByStatus("active", binding.tvCountActive, null)

    }

    private fun getProductByStatus(status: String, textCount: TextView, onSuccessAction: (() -> Unit)?) {
        ProductApiService(token).getProductByStatus(status, { data ->
            textCount.text = data.totalCount.toString()
            textCount.textSize = 24f
            if (onSuccessAction != null) {
                onSuccessAction()
            }
        }, this)
    }

    override fun onStart() {
        super.onStart()

        binding.btnAddProduct.btnComponent.setOnClickListener {
            startActivity(Intent(this, AddProductStepActivity::class.java))
        }
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listDataProduct.collect {
                productListAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {

        binding.rvListProductAll.apply {
            layoutManager = LinearLayoutManager(context)

            // with load adapter
            adapter = productListAdapter.withLoadStateHeaderAndFooter(
                header = ProductsLoadStateAdapter { productListAdapter.retry() },
                footer = ProductsLoadStateAdapter { productListAdapter.retry() }
            )

            productListAdapter.addLoadStateListener { loadState ->
                binding.progressBarListProduct.visibility = View.VISIBLE

                if (loadState.prepend.endOfPaginationReached) {
                    if (productListAdapter.itemCount < 1) {
                        // data empty state
                        binding.progressBarListProduct.visibility = View.GONE
                        // Set text in bellow
                    } else {
                        // data is not empty
                        binding.progressBarListProduct.visibility = View.GONE
                        val dataCount = productListAdapter.itemCount.toString()
                        binding.tvCountAllProduct.text = dataCount
                        binding.tvCountAllProduct.textSize = 24f
                    }
                }
            }
//            setHasFixedSize(true) --> Todo : jika ini di aktifkan, list tidak akan tampil saat initialisasi pertama kali (emang bangsat!)
//            adapter = productListAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ApiService.create(token))
        )[ProductViewModel::class.java]
    }

    override fun onClickItem(data: Product?) {
        if (data != null) {
            val bundle = DataBundle.putProductData(data)
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onErrorGetProductByStatus(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureResponseGetProductByStatus(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun dataChart() {
        pieChart = binding.pieChart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

        pieChart.rotationAngle = 0f

        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1000, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        val entries: ArrayList<PieEntry> = arrayListOf()
        entries.add(PieEntry(7f))
        entries.add(PieEntry(1f))
        entries.add(PieEntry(2f))

        val dataSet = PieDataSet(entries, "Warehouse count")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = arrayListOf()
        colors.add(resources.getColor(R.color.blue_ocean))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.green))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data

        pieChart.highlightValues(null)

        pieChart.invalidate()
    }
}