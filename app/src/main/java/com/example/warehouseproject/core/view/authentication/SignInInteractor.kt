package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.core.model.user.UserRequest


class SignInInteractor {
    interface onFinishedSignInListener {
        fun onInputUsernameError()
        fun onInputPasswordError()
        fun onSuccessValidationSignIn(userRequest: UserRequest)
    }
    fun signInValidation(input: UserRequest, listener: onFinishedSignInListener) {
        when {
            input.username.isEmpty() -> listener.onInputUsernameError()
            input.password.isEmpty() -> listener.onInputPasswordError()
            else -> listener.onSuccessValidationSignIn(input)
        }
    }
}