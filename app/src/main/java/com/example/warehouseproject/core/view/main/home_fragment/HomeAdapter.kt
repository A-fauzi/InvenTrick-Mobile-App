package com.example.warehouseproject.core.view.main.home_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.databinding.ItemDataProductBinding
import com.squareup.picasso.Picasso

class HomeAdapter(
    private val context: Context,
    private val items: ArrayList<Product>,
    private val callClickListener: CallClickListener
    ): RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {
    class MainViewHolder( val binding: ItemDataProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemDataProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        with(holder) {
            with(items[position]) {

                Picasso.get().load(image).centerCrop().resize(500, 500).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemProduct)
                binding.tvNameProduct.text = name

                binding.chipStatus.text = status
                when(status) {
                    "active" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.green_cendol)
                    }
                    "in-progress" -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.red_smooth)
                    }
                    else -> {
                        binding.chipStatus.chipBackgroundColor = context.getColorStateList(R.color.blue)
                    }
                }
                binding.tvItemCodeProduct.text = code_items
                binding.tvSpecProduct.text = specification
                "Quantity: ${qty.toInt()}".also { binding.tvQuantityProduct.text = it }

                binding.itemUserName.text = user.username
                Picasso.get().load(user.user_photo).centerCrop().resize(500, 500).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.itemUserPhoto)

                binding.tvDetailProduct.setOnClickListener {
                    callClickListener.onClickListenerDialog(items[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Product>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface CallClickListener{
        fun onClickListenerDialog(data: Product)
    }

}