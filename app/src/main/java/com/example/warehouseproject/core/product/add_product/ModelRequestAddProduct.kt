package com.example.warehouseproject.core.product.add_product

import android.widget.EditText

data class ModelRequestAddProduct(
    val code: EditText,
    val name: EditText,
    val category: EditText,
    val subCategory: EditText,
//    val image: _root_ide_package_.android.widget.EditText,
    val spec: EditText,
    val price: EditText,
    val location: EditText,
    val status: EditText,
    val model: EditText,
    val codeOracle: EditText,
    val descOracle: EditText,
)
