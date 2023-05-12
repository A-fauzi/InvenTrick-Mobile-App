package com.example.warehouseproject.core.view.main.home_fragment.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.domain.modelentities.product.category.Category
import com.example.warehouseproject.core.utils.helper.SimpleDateFormat
import com.example.warehouseproject.core.service.product.category.ProductCategoryService
import com.example.warehouseproject.core.utils.helper.HideKeyboardHelper
import com.example.warehouseproject.databinding.ActivityProductCategoryBinding
import com.example.warehouseproject.databinding.ItemCreateCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.paperdb.Paper

class ProductCategoryActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ProductCategoryActivity"
    }

    private lateinit var binding: ActivityProductCategoryBinding

    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)

        val token = Paper.book().read<String>(Constant.User.TOKEN).toString()
        ProductCategoryService(token).getCategories {
            adapterCategory = AdapterCategory(this, arrayListOf())
            binding.rvCategory.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterCategory
            }
            adapterCategory.setData(it)
        }
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


        binding.btnAddCategory.setOnClickListener {
            binding.tvResultCategory.text = binding.etCategory.text.toString()
            binding.etSubCategoryProduct.requestFocus()
            binding.outlinedTextCategory.isEnabled = false
        }

        // Logic view
        val getInputArray = arrayListOf<String>()
        var arraySubCategory: List<Category.SubCategory> = subCategory(arrayListOf())

        binding.btnAddSubCategory.setOnClickListener {
            // Ambil input dari EditText dan masukkan ke dalam array
            val input = binding.etSubCategoryProduct.text.toString()

            getInputArray.add(input)

            arraySubCategory = subCategory(getInputArray)

            binding.rvSubCategoryView.apply {
                val mAdapter = AdapterSubCategory(
                    this@ProductCategoryActivity,
                    arraySubCategory as ArrayList<Category.SubCategory>
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = mAdapter
            }



            binding.etSubCategoryProduct.text?.clear()
            HideKeyboardHelper.hideSoftKeyBoard(this, binding.root)

        }

        binding.btnSaveCategory.setOnClickListener {
            binding.btnSaveCategory.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val category = Category(name = binding.tvResultCategory.text.toString(), sub_category = arraySubCategory)

            val token = Paper.book().read<String>("token").toString()

            ProductCategoryService(token).createCategory(category, {onSuccess ->
                Toast.makeText(this, onSuccess.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }, {onError ->
                Toast.makeText(this, onError, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSaveCategory.visibility = View.VISIBLE
            }, {onFailure ->
                Toast.makeText(this, onFailure, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSaveCategory.visibility = View.VISIBLE
            })
        }

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun subCategory(listSub: ArrayList<String>): List<Category.SubCategory> {
        val subCategoryList = arrayListOf<Category.SubCategory>()
        for (i in listSub) {
            val subCategory = Category.SubCategory(
                name = i,
                created_at = SimpleDateFormat.generate()
            )
            subCategoryList.add(subCategory)
        }
        return subCategoryList
    }
}