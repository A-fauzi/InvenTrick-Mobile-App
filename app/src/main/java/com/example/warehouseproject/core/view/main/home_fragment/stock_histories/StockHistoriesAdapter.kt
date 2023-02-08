package com.example.warehouseproject.core.view.main.home_fragment.stock_histories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ItemDataHistoryBinding

class StockHistoriesAdapter(
    private val context: Context,
    private val items: ArrayList<StockHistory>,
    private val lister: Listener
): RecyclerView.Adapter<StockHistoriesAdapter.MainViewHolder>() {
    class MainViewHolder( val binding: ItemDataHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemDataHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
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

                binding.cardItem.setOnClickListener {
                    lister.onClickItemHistory(items[position])
                }

                ProductApiService().getProductByCode(code_items, {}, {
                    binding.tvDataNotFound.text = "Product is avail"
                    binding.tvDataNotFound.setTextColor(context.resources.getColor(R.color.blue)) }, {
                    binding.tvDataNotFound.text = "Product not avail"}
                )

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

    interface Listener {
        fun onClickItemHistory(data: StockHistory)
    }
}