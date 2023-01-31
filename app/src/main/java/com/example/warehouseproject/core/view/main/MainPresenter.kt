package com.example.warehouseproject.core.view.main

import android.content.Context
import android.view.LayoutInflater
import com.example.warehouseproject.R
import com.example.warehouseproject.core.view.product.ModelProduct
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class MainPresenter {
    fun showDetailDialog(layoutInflater: LayoutInflater, context: Context, data: ModelProduct) {
        val binding = ItemDetailDialogBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(context)

        Picasso.get().load(data.image).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivItemDetail)
        binding.statusDetail.text = data.status
        binding.chipCodeItemDetail.text = data.code_items
        binding.tvNameProductDetail.text = data.name

        "quantity: ${data.qty}".also { binding.tvQtyDetail.text = it }
        "price: ${data.price}".also { binding.tvPriceDetail.text = it }
        "location: ${data.location}".also { binding.tvLocationDetail.text = it }

        binding.tvCreatedAtDetail.text = data.created_at
        binding.tvUpdatedAtDetail.text = data.updated_at
        binding.chipCatgory.text = data.category
        binding.chipSubCategory.text = data.sub_category
        binding.tvSpecProductDetail.text = data.specification
        binding.tvCodeOracleDetail.text = data.code_oracle
        binding.tvModelDetail.text = data.model
        binding.tvDescOracleDetail.text = data.description_oracle

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}