package com.example.warehouseproject.example_mvp.login

class LoginPresenter(var loginView: LoginView?, val loginInteractor: LoginInteractor): LoginInteractor.OnLoginFinishedListener {
    fun validateCredential(username: String, password: String) {
        loginView?.showProgress()
        loginInteractor.login(username, password, this)
    }

    fun onDestroy(){
        loginView = null
    }

    override fun onUsernameError() {
        loginView?.apply {
            setUsernameError()
            hideProgress()
        }
    }

    override fun onPasswordError() {
        loginView?.apply {
            setPasswordError()
            hideProgress()
        }
    }

    override fun onSuccess() {
        loginView?.navigateToHome()
    }
}