package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService

class SignInPresenter(private val view: SignInView, private val service: UserApiService, private val interactor: SignInInteractor): UserApiService.OnFinishedRequestUser, SignInInteractor.onFinishedSignInListener {

    fun signInUser(userRequest: UserRequest) {
        service.signInUser(userRequest, this)
        view.onClickBtnLoginView()
    }

    fun validateFormSignIn(userRequest: UserRequest) {
        interactor.signInValidation(userRequest, this)
    }

    override fun onSuccessBody(response: UserResponse.SingleResponse) {
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

    override fun onInputUsernameError() {
        view.onInputUsernameErrorView()
    }

    override fun onInputPasswordError() {
        view.onInputPasswordErrorView()
    }

    override fun onSuccessValidationSignIn(userRequest: UserRequest) {
        view.onSuccessValidationSignIn(userRequest)
    }
}