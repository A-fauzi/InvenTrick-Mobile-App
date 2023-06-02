package com.example.warehouseproject.presentation.view.authentication

import android.text.Editable
import android.widget.EditText
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel

/**
 * Kelas SignInInteractor memiliki satu fungsi signInValidation yang akan memvalidasi masukan dari pengguna.
 * Fungsi ini memiliki dua parameter yaitu input dan listener.
 *
 * input adalah objek UserRequest yang berisi informasi login dari pengguna seperti nama pengguna dan kata sandi.
 * listener adalah objek yang mengimplementasi antarmuka onFinishedSignInListener.
 */
class SignInInteractor {


    interface onFinishedSignInListener {

        /**
         * yang akan dipanggil jika nama pengguna kosong
         */
        fun onInputUsernameError()

        /**
         * yang akan dipanggil jika kata sandi kosong
         */
        fun onInputPasswordError()

        /**
         * yang akan dipanggil jika masukan pengguna valid.
         */
        fun onSuccessValidationSignIn(userRequest: UserAuthRequestModel)
    }

    /**
     * Fungsi signInValidation akan memeriksa apakah nama pengguna atau kata sandi kosong menggunakan when expression.
     * Jika nama pengguna kosong, maka akan dipanggil fungsi onInputUsernameError() dari objek listener.
     * Jika kata sandi kosong, maka akan dipanggil fungsi onInputPasswordError() dari objek listener.
     * Jika kedua-duanya tidak kosong, maka fungsi onSuccessValidationSignIn(input) akan dipanggil dengan argumen input yang diberikan.
     */
    fun signInValidation(input: UserAuthRequestModel, listener: onFinishedSignInListener) {
        when {
            input.username.isEmpty() -> listener.onInputUsernameError()
            input.password.isEmpty() -> listener.onInputPasswordError()
            else -> listener.onSuccessValidationSignIn(input)
        }
    }


    interface OnFinishedTextWatcher {
        fun onInputTextWatcher(text: String, input: EditText)
    }
    fun textWatcher(input: EditText, listener: OnFinishedTextWatcher) = object : android.text.TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            listener.onInputTextWatcher(p0.toString(), input)
        }

    }
}