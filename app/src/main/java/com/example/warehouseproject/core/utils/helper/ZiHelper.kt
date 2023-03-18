package com.example.warehouseproject.core.utils.helper

import android.content.Context
import com.example.warehouseproject.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ZiHelper {
    fun materialAlertDialog(setIcon: Int = 0, title: String, message: String, context: Context, cancleLable: Boolean, onAccept: () -> Unit) {
        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setIcon(setIcon)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancleLable)
            .setNeutralButton("Cancel") { dialog, which ->
                // Respond to neutral button press
            }
            .setNegativeButton("Decline") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Accept") { dialog, which ->
                onAccept()
            }
            .show()
    }
}