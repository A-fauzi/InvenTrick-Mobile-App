package com.example.warehouseproject.core.view.main.home_fragment.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.warehouseproject.core.helper.HideKeyboardHelper
import com.example.warehouseproject.core.helper.SimpleDateFormat
import com.example.warehouseproject.core.model.product.category.CategoryRequest
import com.example.warehouseproject.core.service.product.category.ProductCategoryService
import com.example.warehouseproject.databinding.ActivityProductCategoryBinding
import com.example.warehouseproject.databinding.ItemCreateCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.paperdb.Paper

class ProductCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)
    }

    override fun onStart() {
        super.onStart()

        binding.fabCreateCategory.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val binding = ItemCreateCategoryBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(this)

        // Logic view

        binding.btnAddCategory.setOnClickListener {
            if (binding.etCategory.text.toString().isEmpty()){
                Toast.makeText(this, "colom is required", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvResultCategory.text = binding.etCategory.text.toString()

                // container sub category visible
                binding.rlContainerSubCategory.visibility = View.VISIBLE
                binding.etSubCategory.requestFocus()
                binding.btnAddSubCategory.setOnClickListener {
                    if (binding.etSubCategory.text.toString().isEmpty()) {
                        Toast.makeText(this, "sub category is required", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvResultSubCategory.text = binding.etSubCategory.text.toString()
                        binding.etCategory.text.clear()
                        binding.etSubCategory.text.clear()
                        HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)
                        binding.llContainerForm.visibility = View.GONE
                        binding.tvContainerResult.visibility = View.VISIBLE
                        binding.btnSubmitCategory.visibility = View.VISIBLE
                        binding.btnSubmitCategory.setOnClickListener {
                            binding.progressBar.visibility = View.VISIBLE

                            // get text result
                            Toast.makeText(this, "${binding.tvResultCategory.text} | ${binding.tvResultSubCategory.text}", Toast.LENGTH_SHORT).show()

                            // Store in server
                            val dataSubCategory1 = CategoryRequest.SubCategoryRequest("${binding.tvResultSubCategory.text}", SimpleDateFormat.generate())
                            val arraySub = arrayListOf(dataSubCategory1)
                            val dataCategory1 = CategoryRequest("${binding.tvResultCategory.text}", arraySub)

                            val token = Paper.book().read<String>("token").toString()
                            ProductCategoryService(token).createCategory(dataCategory1,
                                {
                                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                },
                                {
                                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                                    binding.progressBar.visibility = View.GONE
                                },
                                {
                                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                                    binding.progressBar.visibility = View.GONE
                                })
                        }

                    }
                }
            }
        }

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()
    }
}