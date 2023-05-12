package com.example.warehouseproject.core.view.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.warehouseproject.domain.modelentities.user.UserRequest
import com.example.warehouseproject.domain.modelentities.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService

/**
 * Ini adalah kelas MainActivityPresenter yang digunakan untuk mengatur logika bisnis dan komunikasi dengan API untuk tampilan MainActivity
 */
class MainActivityPresenter(
    private val context: Context,
    private val activity: Activity,
    private val view: MainView,
    private val userApiService: UserApiService
): UserApiService.OnFinishedStatusRequest {

    /**
     * Method ini digunakan untuk memeriksa apakah aplikasi memiliki izin untuk mengakses penyimpanan dan kamera.
     * Jika salah satu dari izin-izin tersebut tidak diberikan, method akan meminta izin tersebut kepada pengguna melalui
     */
    fun checkPermission() {
        when (PackageManager.PERMISSION_DENIED) {
            checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    ObjectPermission.WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
                )
            }
            checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    ObjectPermission.READ_EXTERNAL_STORAGE_PERMISSION_CODE
                )
            }
            checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CAMERA),
                    ObjectPermission.CAMERA_PERMISSION_CODE
                )
            }
        }
    }


    /**
     * Method ini digunakan untuk memperbarui status aktivitas pengguna pada server.
     * Method ini membutuhkan tiga parameter: token yang akan digunakan sebagai otorisasi untuk mengakses API, userId sebagai ID pengguna yang status aktivitasnya akan diubah, dan reqStatus sebagai status aktivitas pengguna yang baru.
     * Method ini akan mengirimkan permintaan ke API melalui objek userApiService dan menunggu respon dari server.
     */
    fun updateStatusActivityUser(token: String, userId: String, reqStatus: UserRequest.StatusActivity) {
        userApiService.updateStatusUser(token, userId, reqStatus, this)
    }

    override fun onSuccessBodyReqStatus(response: UserResponse.SingleResponse) {
        view.onSuccessBodyReqStatusView(response)
    }

    override fun onErrorBodyReqStatus(message: String) {
        view.onErrorBodyReqStatusView(message)
    }

    override fun onFailure(message: String) {
        view.onFailureView(message)
    }
}