package com.example.warehouseproject.core.view.main.account_fragment.account_update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.databinding.ActivityAccountUpdateBinding

class AccountUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topAppBar()
    }

    private fun topAppBar() {
        binding.newTxtTopbar.viewStart.visibility = View.GONE
        val arrowBackPress = binding.newTxtTopbar.viewStartArrowBack
        arrowBackPress.visibility = View.VISIBLE
        arrowBackPress.setOnClickListener {
            onBackPressed()
        }

        binding.newTxtTopbar.viewEnd.visibility = View.GONE
        binding.newTxtTopbar.txtTopBar.text = "Edit Profile"
        binding.newTxtTopbar.cvStatusActivityUser.visibility = View.GONE
        binding.newTxtTopbar.tvStatusActivityUser.visibility = View.GONE
    }
}