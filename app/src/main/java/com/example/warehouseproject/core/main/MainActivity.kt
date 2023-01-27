package com.example.warehouseproject.core.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.warehouseproject.R
import com.example.warehouseproject.core.product.add_product.AddProductActivity
import com.example.warehouseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToAddProduct.setOnClickListener {
            navigateToAddProduct()
        }


    }

    override fun navigateToAddProduct() {
        startActivity(Intent(this, AddProductActivity::class.java))
    }
}