package com.example.warehouseproject.core.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.warehouseproject.core.model.product.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductDB: RoomDatabase() {
    abstract fun productDao(): DaoProduct
}