package com.example.warehouseproject.core.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.helper.RealtimeDatabase
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.view.main.account_fragment.AccountFragment
import com.example.warehouseproject.core.view.main.home_fragment.HomeFragment
import com.example.warehouseproject.core.view.main.scan_fragment.ScanFragment
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var realtimeDatabase: RealtimeDatabase

    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)

        realtimeDatabase = RealtimeDatabase(this)

        presenter = MainActivityPresenter(applicationContext, this, this, UserApiService())
        presenter.checkPermission()

        setUpBottomNav()

        val data = UserRequest.StatusActivity("online")
        val token = Paper.book().read<String>(TOKEN).toString()
        val userId = Paper.book().read<String>(ID).toString()
        presenter.updateStatusActivityUser(token, userId, data)

        Paper.book().write("status", data.status_activity)
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

    override fun onStart() {
        super.onStart()

        val data = UserRequest.StatusActivity("online")
        val token = Paper.book().read<String>(TOKEN).toString()
        val userId = Paper.book().read<String>(ID).toString()
        presenter.updateStatusActivityUser(token, userId, data)

        Paper.book().write("status", data.status_activity)
    }

    override fun onDestroy() {
        super.onDestroy()

        val data = UserRequest.StatusActivity("offline")
        val token = Paper.book().read<String>(TOKEN).toString()
        val userId = Paper.book().read<String>(ID).toString()

        presenter.updateStatusActivityUser(token, userId, data)

        Paper.book().write("status", data.status_activity)
    }

    override fun onSuccessBodyReqStatusView(response: UserResponse.SingleResponse) {
        Toast.makeText(this, "status anda ${response.data.status_activity}", Toast.LENGTH_SHORT).show()

        realtimeDatabase.write(response.data._id, UserRequest.StatusActivity(response.data.status_activity))
    }

    override fun onErrorBodyReqStatusView(message: String) {
        Log.d("MainActivity", "onErrorBodyReqStatusView: $message")
    }

    override fun onFailureView(message: String) {
        Log.d("MainActivity", "onFailureView: $message")
    }

}