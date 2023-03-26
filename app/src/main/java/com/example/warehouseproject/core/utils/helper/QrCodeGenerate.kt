package com.example.warehouseproject.core.utils.helper

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.zxing.WriterException

object QrCode {

    fun generate(text: String, setImageBitmap: ImageView, size: Int = 800) {
        val encoder = QRGEncoder(text, null, QRGContents.Type.TEXT, size)
        encoder.colorBlack = Color.WHITE
        encoder.colorWhite = Color.BLACK

        try {
            val bitmap = encoder.bitmap
            setImageBitmap.setImageBitmap(bitmap)
        }catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}