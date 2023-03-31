package com.example.warehouseproject.core.view.main.history_product

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.StockHistory
import com.example.warehouseproject.core.model.user.User
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.databinding.ItemDataHistoryBinding
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class StockHistoryAdapterPaging(
    private val context: Context,
    private val listener: StockHistoriesListener
): PagingDataAdapter<StockHistory, StockHistoryAdapterPaging.ViewHolder>(StockHistoryDiffComp) {
    object StockHistoryDiffComp : DiffUtil.ItemCallback<StockHistory>() {
        override fun areItemsTheSame(oldItem: StockHistory, newItem: StockHistory): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: StockHistory, newItem: StockHistory): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(val binding: ItemDataHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.tvName.text = this?.name

                binding.chipStatus.text = this?.status
                when(this?.status) {
                    "IN" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(com.example.warehouseproject.R.color.green_cendol)
                    }
                    "OUT" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(com.example.warehouseproject.R.color.red_smooth)
                    }
                    else -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(com.example.warehouseproject.R.color.blue)
                    }
                }
                binding.tvItemCode.text = this?.code_items
                "Quantity: ${this?.qty?.toInt()}".also { binding.tvQuantity.text = it }

                val date = this?.created_at?.split(" ")
                val monthYear = "${date?.get(1)} ${date?.get(2)} ${date?.get(3)}"
                val dayTime = "${date?.get(0)} ${date?.get(5)}"

                binding.tvItemMonthYear.text = monthYear
                binding.tvDayTime.text = dayTime

                binding.cardItem.setOnClickListener {
                    getItem(position)?.let { it1 -> listener.onClickItemHistory(it1) }
                }

                Paper.init(context)
                val token = Paper.book().read<String>("token").toString()
                ProductApiService(token).getProductByCode(this?.code_items ?: "", {}, {
                    binding.tvDataNotFound.text = context.getString(R.string.product_is_avail)
                    binding.tvDataNotFound.setTextColor(context.resources.getColor(R.color.blue)) }, {
                    binding.tvDataNotFound.text = context.getString(R.string.product_is_not_avail)
                    binding.tvDataNotFound.setTextColor(context.resources.getColor(R.color.red_smooth))}
                )

                UserApiService().getUserById(token, this?.user_id ?: "null", { onSuccess ->
                    if (onSuccess != null) {
                        binding.tvItemUsername.text = onSuccess.username
                        Glide
                            .with(context)
                            .load(onSuccess.profile_image)
                            .error(R.drawable.img_example)
                            .into(binding.ivItemUserPhoto)
                        binding.ivItemUserPhoto.setOnClickListener {
                            listener.onCLickUserProfile(onSuccess)
                        }

                        Log.d("USER", onSuccess.toString())
                    }
                }, { onError->
                    Log.d("USER", onError)
                }, {onFailure ->
                    Log.d("USER", onFailure)
                })

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDataHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface StockHistoriesListener {
        fun onClickItemHistory(data: StockHistory)
        fun onCLickUserProfile(user: User?)
    }
}