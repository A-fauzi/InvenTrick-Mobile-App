package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.core.model.user.UserResponse

interface SignInView {
    fun onClickBtnLoginView()
    fun onResponseErrorView()
    fun moveToMainActivity()

    // Response message
    fun showResponseMessageSuccess(data: UserResponse.SignIn)
    fun showResponseMessageError(msg: String)
}