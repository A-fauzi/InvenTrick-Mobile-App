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

    /**
     * realtimeDatabase yang merupakan objek dari kelas RealtimeDatabase
     */
    private lateinit var realtimeDatabase: RealtimeDatabase

    /**
     * navController yang merupakan objek dari kelas NavController
     */
    private lateinit var navController: NavController

    /**
     * binding yang merupakan objek dari kelas ActivityMainBinding
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * presenter yang merupakan objek dari kelas MainActivityPresenter
     */
    private lateinit var presenter: MainActivityPresenter


    /**
     *  terdapat fungsi initView() yang dipanggil pada saat onCreate() dijalankan.
     *  Fungsi ini bertugas melakukan inisialisasi objek-objek yang diperlukan pada activity ini.
     */
    private fun initView() {

        presenter = MainActivityPresenter(applicationContext, this, this, UserApiService())
//        presenter.checkPermission()
        Paper.init(this)
        realtimeDatabase = RealtimeDatabase(this)



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        // Check for internet connection
        checkInternetConnection()
    }


    /**
     * Fungsi ini bertujuan untuk memeriksa koneksi internet.
     * Jika perangkat terhubung ke internet, maka akan melakukan pembaruan status aktivitas pengguna menjadi "online" pada database server. Kemudian, fungsi ini akan menyiapkan navigasi menuju halaman utama aplikasi.
     * Namun, jika perangkat tidak terhubung ke internet, maka akan menampilkan halaman pemutusan koneksi internet dan menutup aplikasi.
     * Fungsi ini menggunakan kelas InternetConnect untuk memeriksa koneksi internet.
     * Jika koneksi internet tersedia, fungsi akan memanggil fungsi updateStatusActivityUser() pada presenter dengan parameter token, userId, dan data status pengguna.
     * Fungsi tersebut akan mengirim permintaan ke server untuk memperbarui status aktivitas pengguna.
     * Kemudian, fungsi ini menggunakan kelas Paper untuk menulis status aktivitas pengguna ke penyimpanan lokal dan menyiapkan navigasi menuju halaman utama aplikasi.
     * Namun, jika perangkat tidak terhubung ke internet, fungsi akan menampilkan halaman ItemDisconnectActivity dan menutup aplikasi.

     */
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


    /**
     * Fungsi setUpNavigation() digunakan untuk melakukan pengaturan navigasi antara bottom navigation dengan fragment yang terkait di dalam aplikasi.
     * Pertama, kita dapat menemukan NavHostFragment yang terkait dengan R.id.fragment_container.
     * Kemudian kita dapat mengambil instance dari NavController yang terkait dengan NavHostFragment.
     * Selanjutnya, bottom navigation di inisialisasi dan diatur dengan menggunakan setupWithNavController untuk menentukan navigasi ketika item bottom navigation di klik.
     */
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