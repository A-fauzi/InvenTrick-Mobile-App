package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.home_fragment.ProductListAdapter
import com.example.warehouseproject.databinding.ItemDataProductBinding

class ProductsAdapterPaging(private val context: Context): PagingDataAdapter<Product, ProductsAdapterPaging.ViewHolder>(ProductDiffComp) {
    object ProductDiffComp : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: ItemDataProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                Glide.with(context).load(this?.image).into(binding.ivItemProduct)
                binding.tvItemCodeProduct.text = this?.code_items
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDataProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}