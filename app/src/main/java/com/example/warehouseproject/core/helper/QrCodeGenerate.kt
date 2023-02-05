package com.example.warehouseproject.core.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.warehouseproject.core.model.product.Product
import com.google.zxing.WriterException

object QrCode {

    fun generate(text: String, setImageBitmap: ImageView) {
        val encoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 800)
        encoder.colorBlack = Color.WHITE
        encoder.colorWhite = Color.BLACK

        try {
            setImageBitmap.setImageBitmap(encoder.bitmap)
        }catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}