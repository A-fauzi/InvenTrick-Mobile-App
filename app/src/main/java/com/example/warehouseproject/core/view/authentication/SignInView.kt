package com.example.warehouseproject.core.view.authentication

import com.example.warehouseproject.domain.modelentities.user.UserRequest
import com.example.warehouseproject.domain.modelentities.user.UserResponse

/**
 * Interface SignInView digunakan sebagai kontrak antara presenter dan view untuk
 * melakukan tindakan yang diperlukan saat sign in.
 */
interface SignInView {

    /**
     * Fungsi onClickBtnLoginView dipanggil ketika tombol login di klik.
     */
    fun onClickBtnLoginView()

    /**
     * Fungsi onResponseErrorView dipanggil ketika ada respon error dari server.
     */
    fun onResponseErrorView()

    /**
     * Fungsi moveToMainActivity dipanggil ketika user berhasil sign in dan pindah ke activity utama.
     */
    fun moveToMainActivity()

    /**
     * Fungsi showResponseMessageSuccess dipanggil ketika user berhasil sign in.
     * @param data respon dari server yang berisi data user.
     */
    fun showResponseMessageSuccess(data: UserResponse.SingleResponse)

    /**
     * Fungsi showResponseMessageError dipanggil ketika terjadi kesalahan saat sign in.
     * @param msg pesan error yang akan ditampilkan.
     */
    fun showResponseMessageError(msg: String)

    /**
     * Fungsi onInputUsernameErrorView dipanggil ketika input username tidak valid.
     */
    fun onInputUsernameErrorView()

    /**
     * Fungsi onInputPasswordErrorView dipanggil ketika input password tidak valid.
     */
    fun onInputPasswordErrorView()

    /**
     * Fungsi onSuccessValidationSignIn dipanggil ketika validasi sign in berhasil.
     * @param userRequest data user yang akan digunakan untuk sign in.
     */
    fun onSuccessValidationSignIn(userRequest: UserRequest)
    fun onResponseSignInFailure(msg: String)
}
