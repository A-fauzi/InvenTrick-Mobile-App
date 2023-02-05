package com.example.warehouseproject.core.helper

import android.graphics.Color
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
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