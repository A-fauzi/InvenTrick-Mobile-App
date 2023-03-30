package com.example.warehouseproject.core.view.product.add_product.steps

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.databinding.ItemAddProductChooseCategoryBinding

class AdapterSubCategoryAddProduct(
    private val context: Context,
    private val items: ArrayList<Category.SubCategory>
) : RecyclerView.Adapter<AdapterSubCategoryAddProduct.ViewHolder>() {
    inner class ViewHolder(val binding: ItemAddProductChooseCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddProductChooseCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.tvItemCategory.text = name
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Category.SubCategory>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface AdapterSubCategoryListener {
        fun onClickItem(category: Category.SubCategory)
    }
}