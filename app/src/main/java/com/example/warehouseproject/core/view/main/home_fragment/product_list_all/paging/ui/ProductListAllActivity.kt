package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.utils.CustomPieChart
import com.example.warehouseproject.core.utils.DataBundle
import com.example.warehouseproject.core.view.main.detail_product.DetailProductActivity
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.api.ApiService
import com.example.warehouseproject.core.view.product.add_product.steps.AddProductStepActivity
import com.example.warehouseproject.databinding.ActivityProductListAllBinding
import com.example.warehouseproject.databinding.ItemLabelChartBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import io.paperdb.Paper
import kotlinx.coroutines.launch
import java.text.NumberFormat

class ProductListAllActivity : AppCompatActivity(), ProductsAdapterPaging.ProductsListenerPaging,
    ProductApiService.OnFinishedGetProductByStatus {

    private lateinit var binding: ActivityProductListAllBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListAdapter: ProductsAdapterPaging
    private lateinit var pieChart: PieChart

    private val token = Paper.book().read<String>("token").toString()

    private fun initView() {
        Paper.init(this)
//        binding.btnAddProduct.btnComponent.text = getString(R.string.add_product)
        productListAdapter = ProductsAdapterPaging(applicationContext, this)

        // Pie chart
        pieChart = binding.pieChart
        pieChart.setNoDataText("Sedang memuat data chart")
        pieChart.setNoDataTextColor(resources.getColor(R.color.font_color_default))
        pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)

        // Label chart
        val includeItemLabelActive = binding.tvLabelActive
        val includeItemLabelProgress = binding.tvLabelOnProgress
        viewLabelChart(includeItemLabelActive, getString(R.string.active), getColorStateList(R.color.green))
        viewLabelChart(includeItemLabelProgress, getString(R.string.on_progress), getColorStateList(R.color.red_smooth))
    }

    private fun getCountByStatus(dataActive: (count: Int) -> Unit, dataOnProgress: (count: Int) -> Unit) {
        getProductByStatus("active"){
            dataActive(it)
        }
        getProductByStatus("in-progress"){
            dataOnProgress(it)
        }
    }

    private fun viewLabelChart(includeView: ItemLabelChartBinding, setTextLabel: String, bgColorView: ColorStateList?) {
        includeView.tvLabelChart.text = setTextLabel
        includeView.cvCircleColorChartLabel.setCardBackgroundColor(bgColorView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
        setupList()
        setupView()
    }

    private fun getProductByStatus(status: String, onSuccessAction: ((count: Int) -> Unit)?) {
        ProductApiService(token).getProductByStatus(status, { data ->
            if (onSuccessAction != null) {
                onSuccessAction(data.totalCount)
            }
        }, this)
    }

    override fun onStart() {
        super.onStart()

        binding.fabAddProduct.setOnClickListener {
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
                        val dataAllCount = productListAdapter.itemCount

                        var act = 0
                        var prog = 0


                        getCountByStatus(
                            { active ->
                                act = active
                                dataChart(act.toFloat(), prog.toFloat(), dataAllCount.toString())
                            }, { progress ->
                                prog = progress
                                dataChart(act.toFloat(), prog.toFloat(), dataAllCount.toString())
                            })

                    }
                }
            }
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

    private fun dataChart(active: Float, onProgress: Float, allProduct: String) {
        pieChart.setExtraOffsets(40f, 0f, 40f, 0f)

        // Custom renderer used to add dots at the end of value lines
        pieChart.renderer = CustomPieChart(pieChart, 10f)

        val dataSet = PieDataSet(listOf(
            PieEntry(active),
            PieEntry(onProgress)
        ), "Pie Chart")

        // Chart colors
        val colors = listOf(
            resources.getColor(R.color.green),
            resources.getColor(R.color.red_smooth)
        )
        dataSet.colors = colors
        dataSet.setValueTextColors(colors)

        // Value lines
        dataSet.valueLinePart1Length = 0.6f
        dataSet.valueLinePart2Length = 0.3f
        dataSet.valueLineWidth = 2f
        dataSet.valueLinePart1OffsetPercentage = 115f // Line starts outside of chart
        dataSet.isUsingSliceColorAsValueLineColor = true

        // Value text appearance
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueTextSize = 16f
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

        // Value Formatting
        dataSet.valueFormatter = object : ValueFormatter() {
            private val formatter = NumberFormat.getPercentInstance()

            override fun getFormattedValue(value: Float) = formatter.format(value / 100f)
        }
        pieChart.setUsePercentValues(true)

        dataSet.selectionShift = 3f

        // Hole
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 50f

        // Center text
        pieChart.setDrawCenterText(true)
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.setCenterTextColor(Color.parseColor("#222222"))
        pieChart.centerText = "$allProduct\nProduct"

        // animation in
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // Disable legend & description
        pieChart.legend.isEnabled = false
        pieChart.description = null

        pieChart.data = PieData(dataSet)
    }
}