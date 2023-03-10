package com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import coil.load
import com.example.awesomedialog.*
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.helper.*
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import io.paperdb.Paper

class DetailDialog {

    interface DetailDialogOnFinished {
        fun onSuccessDeleted(data: Product?)
    }

    fun showDialog(context: Activity, layoutInflater: LayoutInflater, data: Product, listener: DetailDialogOnFinished) {
        Paper.init(context)

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

        val idCurrentUser = Paper.book().read<String>(Constant.User.ID).toString()

        if (data.user._id != idCurrentUser) {
            binding.tvBtnTrash.visibility = View.GONE
            binding.tvBtnUpdate.visibility = View.GONE
            binding.btnProdOut.visibility = View.GONE
            binding.btnProdIn.visibility = View.GONE
        }

        val positionCurrentUser = Paper.book().read<String>(Constant.User.DIVISION).toString()
        if (positionCurrentUser == "Receiving") {
            binding.btnProdOut.visibility = View.GONE
        } else if (positionCurrentUser == "Finish Good") {
            binding.btnProdIn.visibility = View.GONE
        }

        binding.tvBtnTrash.setOnClickListener {

            AwesomeDialog.build(context)
                .title("Delete item!", null, context.resources.getColor(R.color.black))
                .body("Are u sure this delete item?", null, context.resources.getColor(R.color.black))
                .position(AwesomeDialog.POSITIONS.CENTER)
                .onPositive("Next", null, null) {
                    fun deleteItem(){
                        binding.progressDelete.visibility = View.VISIBLE
                        binding.llFullContainer.visibility = View.GONE

                        val token = Paper.book().read<String>("token").toString()
                        ProductApiService(token).deleteProductApiService(data._id, { msg, data ->
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
                    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
                    val refStorageDelete = firebaseStorage.reference.child(data.path_storage)
                    refStorageDelete.delete().addOnSuccessListener {
                        binding.progressDelete.visibility = View.VISIBLE
                        binding.llFullContainer.visibility = View.GONE
                        deleteItem()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                .onNegative("Cancel")

        }

        binding.tvBtnUpdate.setOnClickListener {
            Toast.makeText(context, "Belum bisa digunakan", Toast.LENGTH_SHORT).show()
        }

        val bundle = Bundle()
        bundle.putString("code_items_key", data.code_items)
        binding.btnProdIn.setOnClickListener {
            val intent = Intent(context, StockInActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        binding.btnProdOut.setOnClickListener {
            val intent = Intent(context, StockOutActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        // Encoder qrcode
        val text = data.code_items
        QrCode.generate(text, binding.ivQrCode)

        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.show()

    }
}