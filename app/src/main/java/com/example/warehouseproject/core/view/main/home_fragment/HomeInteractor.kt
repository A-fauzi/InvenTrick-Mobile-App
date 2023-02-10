package com.example.warehouseproject.core.view.main.home_fragment

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.Currency
import com.example.warehouseproject.core.helper.PreferenceHelper.loadData
import com.example.warehouseproject.core.helper.QrCode
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.service.product.ProductApiService
import com.example.warehouseproject.databinding.ItemDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HomeInteractor {

}