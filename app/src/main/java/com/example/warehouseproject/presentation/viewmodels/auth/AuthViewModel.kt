package com.example.warehouseproject.presentation.viewmodels.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouseproject.data.repositoryimpl.user.AuthRepository
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    fun login(userAuthRequestModel: UserAuthRequestModel) {
        authRepository.signInUser(userAuthRequestModel, object : AuthRepository.OnFinishedRequestUser{
            override fun onSuccessBody(response: UserResponseModel.SingleResponse) {
                _authResult.postValue(AuthResult.Success(response))
            }

            override fun onErrorBody(message: String) {
                _authResult.postValue(AuthResult.Error(message))
            }

            override fun onFailure(message: String) {
                _authResult.postValue(AuthResult.Failure(message))
            }

        })
    }

    sealed class AuthResult {
        class Success(val response: UserResponseModel.SingleResponse): AuthResult()
        class Error(val msg: String): AuthResult()
        class Failure(val msg: String): AuthResult()
    }
}