package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.home_fragment.ProductListAdapter
import com.example.warehouseproject.databinding.ItemDataProductBinding
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class ProductsAdapterPaging(
    private val context: Context,
    private val listenerPaging: ProductsListenerPaging
    ): PagingDataAdapter<Product, ProductsAdapterPaging.ViewHolder>(ProductDiffComp) {
    object ProductDiffComp : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: ItemDataProductBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                val currentUser = Paper.book().read<String>(Constant.User.USERNAME).toString()
                if (currentUser == this?.user?.username) { binding.itemUserName.text = "Anda" } else { binding.itemUserName.text  = this?.user?.username }

                binding.tvTimeDiffCalculate.text = "Upload time: \n${this?.created_at} WIB"
                Glide.with(context).load(this?.image).into(binding.ivItemProduct)
                binding.tvQuantityProduct.text = "quantity: ${this?.qty}"
                binding.tvItemCodeProduct.text = this?.code_items
                binding.tvSpecProduct.text = this?.specification
                binding.tvNameProduct.text = this?.name
                binding.chipStatus.text = this?.status
                Picasso
                    .get()
                    .load(this?.user?.profile_image)
                    .placeholder(R.drawable.ic_people)
                    .error(R.drawable.img_example)
                    .into(binding.itemUserPhoto)

                binding.tvDetailProduct.setOnClickListener {
                    listenerPaging.onClickItem(getItem(position))
                }

                when(this?.status) {
                    "active" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.green)
                    }
                    "in-progress" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.red_smooth)
                    }
                    else -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.blue)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDataProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface ProductsListenerPaging {
        fun onClickItem(data: Product?)
    }
}