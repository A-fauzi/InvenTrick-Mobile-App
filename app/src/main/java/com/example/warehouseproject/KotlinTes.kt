package com.example.warehouseproject




fun main() {
    val a = arrayOf("PX0003", "PX0004")
    val b = arrayOf("PX0002", "PX0002", "PX0003")


    for (i in b) {
        if (a.contains(i)) println("data ada")
        else println("data tidak ada")
    }


}