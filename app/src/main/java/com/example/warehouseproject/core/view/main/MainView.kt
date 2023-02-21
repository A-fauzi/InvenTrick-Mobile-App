package com.example.warehouseproject.core.view.main

import com.example.warehouseproject.core.model.user.UserResponse

interface MainView {
    fun onSuccessBodyReqStatusView(response: UserResponse.SingleResponse)
    fun onErrorBodyReqStatusView(message: String)
    fun onFailureView(message: String)
}