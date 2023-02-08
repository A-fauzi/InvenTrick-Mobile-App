package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.Currency
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
        when(data.status) {
            "active" -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.green_cendol)
            }
            "sold" -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.red_smooth)
            }
            else -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.blue)
            }
        }

        binding.chipCodeItemDetail.text = data.code_items
        binding.tvNameProductDetail.text = data.name

        "quantity: ${data.qty}".also { binding.tvQtyDetail.text = it }
        binding.tvPriceDetail.text = Currency.format(data.price.toDouble(), "id", "ID")
        "location: ${data.location}".also { binding.tvLocationDetail.text = it }

        binding.tvCreatedAtDetail.text = data.created_at
        binding.tvUpdatedAtDetail.text = data.updated_at
        binding.chipCatgory.text = data.category
        binding.chipSubCategory.text = data.sub_category
        binding.tvSpecProductDetail.text = data.specification
        binding.tvLotDetail.text = data.lot
        binding.tvModelDetail.text = data.model
        "exp: ${data.exp}".also { binding.tvExpDetail.text = it }

        binding.tvBtnTrash.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            with(builder) {
                fun deleteItem(){
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
                setTitle("Delete item")
                setMessage("are u sure this delete item?")
                setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    deleteItem()
                })
                setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialogInterface, i ->
                    dialog.cancel()
                })
                show()
            }

        }

        // Encoder qrcode
        val text = data.code_items
        QrCode.generate(text, binding.ivQrCode)

        binding.btnSaveBarcode.setOnClickListener {
            Toast.makeText(context, "save image clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}