package com.example.warehouseproject.core.view.main.home_fragment.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.databinding.ItemSubCategoryBinding

class AdapterSubCategory(
    private val context: Context,
    private val items: ArrayList<Category.SubCategory>
):  RecyclerView.Adapter<AdapterSubCategory.ViewHolder>() {
    class ViewHolder(val binding: ItemSubCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.tvItemSubCategory.text = name
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
}