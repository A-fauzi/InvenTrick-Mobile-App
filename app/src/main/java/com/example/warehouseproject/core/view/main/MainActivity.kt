package com.example.warehouseproject.core.view.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.warehouseproject.R
import com.example.warehouseproject.core.view.main.account_fragment.AccountFragment
import com.example.warehouseproject.core.view.main.home_fragment.HomeFragment
import com.example.warehouseproject.core.view.main.scan_fragment.ScanFragment
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainActivityPresenter(applicationContext, this)
        presenter.checkPermission()

        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        presenter.loadFragment(HomeFragment(), this)

        bottomNavigationView = binding.bottomNav
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    presenter.loadFragment(HomeFragment(), this)
                    true
                }
                R.id.scan -> {
                    presenter.loadFragment(ScanFragment(), this)
                    true
                }
                R.id.account -> {
                    presenter.loadFragment(AccountFragment(), this)
                    true
                }
                else -> false
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ObjectPermission.WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            ObjectPermission.READ_EXTERNAL_STORAGE_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            ObjectPermission.CAMERA_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Anda perlu memberikan semua izin untuk menggunakan aplikasi ini.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}