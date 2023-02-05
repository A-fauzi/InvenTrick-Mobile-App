package com.example.warehouseproject.core.room.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouseproject.core.model.product.Product

@Dao
interface DaoProduct {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Product>

    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(product: Product)
}