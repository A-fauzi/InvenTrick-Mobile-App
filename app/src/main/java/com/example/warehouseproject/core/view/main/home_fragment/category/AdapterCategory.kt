package com.example.warehouseproject.core.view.main.home_fragment.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.databinding.ItemsCategoryBinding

class AdapterCategory(
    private val context: Context,
    private val items: MutableList<Category>
): RecyclerView.Adapter<AdapterCategory.ViewHolder>() {
    private lateinit var adapterSubCategory: AdapterSubCategory
    class ViewHolder(val binding: ItemsCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.tvItemCategory.text = name
                adapterSubCategory = AdapterSubCategory(context, arrayListOf())
                binding.rvSubCategory.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = adapterSubCategory
                }
                if (sub_category != null) {
                    adapterSubCategory.setData(sub_category)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<Category>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}