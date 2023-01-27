package com.example.warehouseproject.core.product.add_product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.main.MainActivity
import com.example.warehouseproject.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity(), AddProductView {

    private val presenter = AddProductPresenter(this, AddProductInteractor())
    private lateinit var modelRequestAddProduct: ModelRequestAddProduct

    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelRequestAddProduct = ModelRequestAddProduct(
            binding.etCodeProduct,
            binding.etNameProduct,
            binding.etCategoryProduct,
            binding.etSubCategoryProduct,
//         binding.ivChooseImage,
            binding.etSpecProduct,
            binding.etPriceProduct,
            binding.etLocationProduct,
            binding.etStatusProduct,
            binding.etModelProduct,
            binding.etCodeOracleProduct,
            binding.etDescOracleProduct,
        )

        binding.submitButtonAddProduct.setOnClickListener {
            validateCheckInput(modelRequestAddProduct)
        }
    }

    private fun validateCheckInput(request: ModelRequestAddProduct) {
        presenter.validateAddProduct(request)
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setInputError() {
        Toast.makeText(this, "Column is required", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun getDataProduct() {
        Log.d("MainActivity", "Success validate ${binding.etNameProduct.text}")
    }
}