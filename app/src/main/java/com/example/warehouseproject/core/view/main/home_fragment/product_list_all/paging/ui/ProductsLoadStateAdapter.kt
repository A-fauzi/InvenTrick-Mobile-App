package com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseproject.databinding.ItemLoadingStateBinding

class ProductsLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<ProductsLoadStateAdapter.ProductsLoadStateViewHolder>() {
    inner class ProductsLoadStateViewHolder(
        private val binding: ItemLoadingStateBinding,
        private val retry: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textViewError.text = loadState.error.localizedMessage
            }

            binding.progressbar.isVisible = (loadState is LoadState.Loading)
            binding.buttonRetry.isVisible = (loadState is LoadState.Error)
            binding.textViewError.isVisible = (loadState is LoadState.Error)
            binding.buttonRetry.setOnClickListener {
                retry()
            }

            binding.progressbar.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: ProductsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = ProductsLoadStateViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )

}