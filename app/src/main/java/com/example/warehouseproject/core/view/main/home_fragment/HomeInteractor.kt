package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.QrCode
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class HomeInteractor {

    interface HomeInteractorContract {
        fun onSuccessDeleted()
    }

    fun showDialog(context: Context, layoutInflater: LayoutInflater, data: Product, listener: HomeInteractorContract) {
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
        binding.tvLotDetail.text = data.lot
        binding.tvModelDetail.text = data.model
        "exp: ${data.exp}".also { binding.tvExpDetail.text = it }

        binding.ivBtnTrash.setOnClickListener {

            binding.progressDelete.visibility = View.VISIBLE
            binding.llFullContainer.visibility = View.GONE

            ProductApiService().deleteProductApiService(data._id, {msg, data ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                listener.onSuccessDeleted()
            }, { msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                binding.progressDelete.visibility = View.GONE
                binding.llFullContainer.visibility = View.VISIBLE
            }, { msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                binding.progressDelete.visibility = View.GONE
                binding.llFullContainer.visibility = View.VISIBLE
            })
        }

        // Encoder qrcode
        val text = """
            code_items: ${data.code_items}
            name: ${data.name}
            quantity: ${data.qty}
        """.trimIndent()
        QrCode.generate(text, binding.ivQrCode)

        binding.btnSaveBarcode.setOnClickListener {
            Toast.makeText(context, "save image clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}