package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService

class SignInPresenter(private val view: SignInView, private val service: UserApiService): UserApiService.OnFinishedRequestUser {

    fun signInUser(userRequest: UserRequest) {
        service.signInUser(userRequest, this)
        view.onClickBtnLoginView()
    }

    override fun onSuccessBody(response: UserResponse.SignIn) {
        view.moveToMainActivity()
        view.showResponseMessageSuccess(response)
    }

    override fun onErrorBody(message: String) {
        view.onResponseErrorView()
        view.showResponseMessageError(msg = message)
    }

    override fun onFailure(message: String) {
        view.onResponseErrorView()
        view.showResponseMessageError(msg = message)
    }
}