package com.example.warehouseproject.core.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object TextWatcher {
    fun addTextCangedListener(editText: EditText, onTextChanged: (p0: CharSequence?) -> Unit) {
        val textWatcher =  object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        editText.addTextChangedListener(textWatcher)

    }
}