package com.example.warehouseproject.core.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.R
import com.example.warehouseproject.core.view.product.ModelProduct
import com.example.warehouseproject.databinding.ItemDataProductBinding
import com.squareup.picasso.Picasso

class MainAdapter(private val items: ArrayList<ModelProduct>): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    class MainViewHolder( val binding: ItemDataProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemDataProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Picasso
                    .get()
                    .load(image)
                    .placeholder(R.drawable.ic_people)
                    .error(R.drawable.img_example)
                    .into(binding.ivItemProduct)
                binding.tvNameProduct.text = name
                binding.tvSpecProduct.text = specification
                "Quantity: ${qty.toInt()}".also { binding.tvQuantityProduct.text = it }
                binding.tvDetailProduct.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ModelProduct>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface CallClickListener{
        fun onClickListener(data: ModelProduct)
    }

}