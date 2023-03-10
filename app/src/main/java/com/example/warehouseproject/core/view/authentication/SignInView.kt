package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse

interface SignInView {
    fun onClickBtnLoginView()
    fun onResponseErrorView()
    fun moveToMainActivity()

    // Response message
    fun showResponseMessageSuccess(data: UserResponse.SingleResponse)
    fun showResponseMessageError(msg: String)

    // Validation
    fun onInputUsernameErrorView()
    fun onInputPasswordErrorView()
    fun onSuccessValidationSignIn(userRequest: UserRequest)
}