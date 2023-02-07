package com.example.warehouseproject.core.view.main.home_fragment.stock_histories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.databinding.ItemDataHistoryBinding
import com.example.warehouseproject.databinding.ItemDataProductBinding
import com.squareup.picasso.Picasso

class StockHistoriesAdapter(
    private val context: Context,
    private val items: ArrayList<StockHistory>
): RecyclerView.Adapter<StockHistoriesAdapter.MainViewHolder>() {
    class MainViewHolder( val binding: ItemDataHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemDataHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.tvName.text = name

                binding.chipStatus.text = status
                when(status) {
                    "IN" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.green_cendol)
                    }
                    "OUT" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.red_smooth)
                    }
                    else -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.blue)
                    }
                }
                binding.tvItemCode.text = code_items
                "Quantity: ${qty.toInt()}".also { binding.tvQuantity.text = it }
                binding.tvDate.text = created_at
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<StockHistory>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

}