package com.example.warehouseproject.core.view.main


import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.User.ID
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.core.utils.helper.InternetConnect
import com.example.warehouseproject.core.utils.helper.RealtimeDatabase
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.view.not_internet_connect.ItemDisconnectActivity
import com.example.warehouseproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var realtimeDatabase: RealtimeDatabase
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var presenter: MainActivityPresenter

    private fun initView() {
        Paper.init(this)

        realtimeDatabase = RealtimeDatabase(this)

        presenter = MainActivityPresenter(applicationContext, this, this, UserApiService())
        presenter.checkPermission()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        // Check for internet connection
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        if (InternetConnect.checkInternetConnect(this)) {

            val data = UserRequest.StatusActivity("online")
            val token = Paper.book().read<String>(TOKEN).toString()
            val userId = Paper.book().read<String>(ID).toString()
            presenter.updateStatusActivityUser(token, userId, data)

            Paper.book().write("status", data.status_activity)

            setUpNavigation()
        } else {
            startActivity(Intent(this, ItemDisconnectActivity::class.java))
            finish()
        }
    }


    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigation = binding.bottomNav
        setupWithNavController(bottomNavigation, navController)
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
        realtimeDatabase.write(response.data._id, UserRequest.StatusActivity(response.data.status_activity))
    }

    override fun onErrorBodyReqStatusView(message: String) {
        Log.d("MainActivity", "onErrorBodyReqStatusView: $message")
    }

    override fun onFailureView(message: String) {
        Log.d("MainActivity", "onFailureView: $message")
    }

}