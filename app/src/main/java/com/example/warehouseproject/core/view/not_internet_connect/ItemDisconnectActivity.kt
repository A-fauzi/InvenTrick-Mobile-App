package com.example.warehouseproject.core.view.not_internet_connect

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.warehouseproject.core.helper.InternetConnect
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ItemDialogNotConnectionInternetBinding

class ItemDisconnectActivity : AppCompatActivity() {

    private lateinit var binding: ItemDialogNotConnectionInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDialogNotConnectionInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRefreshConnection.setOnClickListener {
            if (InternetConnect.checkInternetConnect(this)) {
                Toast.makeText(this, "Connected again", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please check connection internet", Toast.LENGTH_SHORT).show()
            }
        }
    }
}