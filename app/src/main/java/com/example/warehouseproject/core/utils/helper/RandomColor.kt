package com.example.warehouseproject.core.utils.helper

object RandomColor {
    fun generate(): String = arrayListOf("#7DA0FA", "#4747A1", "#7978E9", "#F3797E").random()

    fun softColor(): String = arrayListOf("#EDCCE4", "#E7D6E7", "#E0E0EB", "#DAE9EE", "#D3F3F1").random()
}