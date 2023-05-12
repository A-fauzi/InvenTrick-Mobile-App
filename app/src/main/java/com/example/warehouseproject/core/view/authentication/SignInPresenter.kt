package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.domain.modelentities.user.UserRequest
import com.example.warehouseproject.domain.modelentities.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService


/**
 * Class SignInPresenter berfungsi untuk menghubungkan antara view dan interactor.
 * Presenter akan mengirimkan permintaan sign in ke interactor dan menerima respon dari interactor.
 * Presenter juga akan menerima respon dari service ketika permintaan sign in dikirimkan.
 * @param view tampilan untuk sign in.
 * @param service objek untuk mengirimkan permintaan sign in ke server.
 * @param interactor objek untuk memvalidasi data sign in.
 */
class SignInPresenter(private val view: SignInView, private val service: UserApiService, private val interactor: SignInInteractor): UserApiService.OnFinishedRequestUser, SignInInteractor.onFinishedSignInListener {


    /**
     * Fungsi signInUser dipanggil ketika user ingin sign in.
     * Fungsi ini akan mengirimkan permintaan sign in ke server menggunakan objek service.
     * @param userRequest data user yang akan digunakan untuk sign in.
     */
    fun signInUser(userRequest: UserRequest) {
        service.signInUser(userRequest, this)
        view.onClickBtnLoginView()
    }


    /**
     * Fungsi validateFormSignIn dipanggil untuk memvalidasi data sign in.
     * Fungsi ini akan mengirimkan data user yang akan digunakan untuk sign in ke interactor untuk divalidasi.
     * @param userRequest data user yang akan digunakan untuk sign in.
     */
    fun validateFormSignIn(userRequest: UserRequest) {
        interactor.signInValidation(userRequest, this)
    }


    /**
     * Fungsi onSuccessBody dipanggil ketika permintaan sign in berhasil dilakukan dan menerima respon dari server.
     * Fungsi ini akan memanggil fungsi view.moveToMainActivity() dan view.showResponseMessageSuccess(data: UserResponse.SingleResponse).
     * @param response respon dari server yang berisi data user.
     */
    override fun onSuccessBody(response: UserResponse.SingleResponse) {
        view.moveToMainActivity()
        view.showResponseMessageSuccess(response)
    }

    /**
     * Fungsi onErrorBody dipanggil ketika terjadi kesalahan saat permintaan sign in dilakukan.
     * Fungsi ini akan memanggil fungsi view.onResponseErrorView() dan view.showResponseMessageError(msg: String).
     * @param message pesan error yang diterima dari server.
     */
    override fun onErrorBody(message: String) {
        view.onResponseErrorView()
        view.showResponseMessageError(msg = message)
    }

    /**
     * Fungsi onFailure dipanggil ketika terjadi kesalahan pada koneksi atau server.
     * Fungsi ini akan memanggil fungsi view.onResponseErrorView() dan view.showResponseMessageError(msg: String).
     * @param message pesan error yang diterima dari koneksi atau server.
     */
    override fun onFailure(message: String) {
        view.onResponseErrorView()
        view.onResponseSignInFailure(msg = message)
    }


    /**
     * Fungsi onInputUsernameError dipanggil ketika input username tidak valid.
     * Fungsi ini akan memanggil fungsi view.onInputUsernameErrorView().
     */
    override fun onInputUsernameError() {
        view.onInputUsernameErrorView()
    }


    /**
     * Fungsi onInputPasswordError dipanggil ketika input password tidak valid.
     * Fungsi ini akan memanggil fungsi view.onInputPasswordErrorView().
     */
    override fun onInputPasswordError() {
        view.onInputPasswordErrorView()
    }


    /**

    Fungsi onSuccessValidationSignIn dipanggil ketika data sign in sudah divalidasi dan valid.
    Fungsi ini akan memanggil fungsi view.onSuccessValidationSignIn(userRequest: UserRequest).
    @param userRequest data user yang sudah divalidasi dan valid.
     */
    override fun onSuccessValidationSignIn(userRequest: UserRequest) {
        view.onSuccessValidationSignIn(userRequest)
    }
}