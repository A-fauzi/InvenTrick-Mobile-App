package com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.Currency
import com.example.warehouseproject.core.helper.PreferenceHelper
import com.example.warehouseproject.core.helper.QrCode
import com.example.warehouseproject.core.helper.RandomColor
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage

class DetailDialog {
    interface DetailDialogOnFinished {
        fun onSuccessDeleted(data: Product?)
    }

    fun showDialog(context: Context, layoutInflater: LayoutInflater, data: Product, listener: DetailDialogOnFinished) {
        val binding = ItemDetailDialogBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(context)

        binding.ivItemDetail.load(data.image) {
            crossfade(true)
            placeholder(R.drawable.ic_people)
        }

        binding.chipCatgory.setOnClickListener {
            Toast.makeText(context, "category masih kosong", Toast.LENGTH_SHORT).show()
        }
        binding.chipSubCategory.setOnClickListener {
            Toast.makeText(context, "sub category masih kosong", Toast.LENGTH_SHORT).show()
        }

        binding.statusDetail.text = data.status
        when(data.status) {
            "active" -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.green_cendol)
            }
            "in-progress" -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.red_smooth)
            }
            else -> {
                binding.statusDetail.chipBackgroundColor = context.getColorStateList(R.color.blue)
            }
        }

        binding.chipCodeItemDetail.text = data.code_items
        binding.tvNameProductDetail.text = data.name

        binding.tvQtyDetail.text = data.qty
        binding.tvPriceDetail.text = Currency.format(data.price.toDouble(), "id", "ID")
        binding.tvLocationDetail.text = data.location

        binding.tvCreatedAtDetail.text = data.created_at
        binding.tvUpdatedAtDetail.text = data.updated_at
        binding.chipCatgory.text = data.category
        binding.chipSubCategory.text = data.sub_category
        binding.tvSpecProductDetail.text = data.specification
        binding.tvLotDetail.text = data.lot
        binding.tvModelDetail.text = data.model
        binding.tvExpDetail.text = data.exp

        binding.tvBtnTrash.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            with(builder) {
                fun deleteItem(){
                    binding.progressDelete.visibility = View.VISIBLE
                    binding.llFullContainer.visibility = View.GONE

                    ProductApiService().deleteProductApiService(data._id, { msg, data ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        listener.onSuccessDeleted(data)
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

                    val dataSharedPref = PreferenceHelper.loadData(context, data.code_items)
                    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
                    val refStorageDelete = firebaseStorage.reference.child(dataSharedPref ?: "No Data In Shared")
                    refStorageDelete.delete().addOnSuccessListener {
                        binding.progressDelete.visibility = View.VISIBLE
                        binding.llFullContainer.visibility = View.GONE
                        deleteItem()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }

                })
                setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialogInterface, i ->
                    dialog.cancel()
                })
                show()
            }

        }

        binding.tvBtnUpdate.setOnClickListener {
            Toast.makeText(context, "Belum bisa digunakan", Toast.LENGTH_SHORT).show()
        }

        // Encoder qrcode
        val text = data.code_items
        QrCode.generate(text, binding.ivQrCode)

        binding.btnSaveBarcode.setOnClickListener {
            Toast.makeText(context, "Belum bisa digunakan", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}