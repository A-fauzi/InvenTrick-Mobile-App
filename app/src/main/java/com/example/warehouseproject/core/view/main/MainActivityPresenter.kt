package com.example.warehouseproject.core.view.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.warehouseproject.R
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService

class MainActivityPresenter(
    private val context: Context,
    private val activity: Activity,
    private val view: MainView,
    private val userApiService: UserApiService
): UserApiService.OnFinishedStatusRequest {
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