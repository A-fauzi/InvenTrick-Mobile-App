package com.example.warehouseproject.core.view.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.User.DIVISION
import com.example.warehouseproject.core.constant.Constant.User.EMAIL
import com.example.warehouseproject.core.constant.Constant.User.FULLNAME
import com.example.warehouseproject.core.constant.Constant.User.ID
import com.example.warehouseproject.core.constant.Constant.User.PROFILE_PHOTO
import com.example.warehouseproject.core.constant.Constant.User.STORAGE_PATH_PROFILE
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.core.constant.Constant.User.USERNAME
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.utils.helper.ZiHelper
import com.example.warehouseproject.core.utils.helper.ZiHelper.openWa
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivitySignInBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class SignInActivity : AppCompatActivity(), SignInView {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var presenter: SignInPresenter
    private lateinit var btnLogin: Button
    private var id = "null"

    private fun initView() {
        btnLogin = binding.btnComponentLogin.btnComponent
        btnLogin.text = getString(R.string.login)

        Paper.init(this)
        presenter = SignInPresenter(this, UserApiService(), SignInInteractor())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        id = Paper.book().read<String>(ID).toString()

    }

    override fun onStart() {
        super.onStart()

        checkCurrentUser()
        buttonAction()
        editTextSetTextWatcher()

        Picasso.get().load("https://i.pinimg.com/originals/ed/0a/a7/ed0aa728d861d69cdce28fb3055f9fd9.gif").into(binding.ivContent)

    }

    private fun checkCurrentUser() {
        if (id != "null") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun editTextSetTextWatcher() {
        binding.etEmail.addTextChangedListener(textWatcher(binding.etEmail))
        binding.etPassword.addTextChangedListener(textWatcher(binding.etPassword))
    }

    private fun buttonAction() {
        btnLogin.setOnClickListener {
            val request = UserRequest(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            presenter.validateFormSignIn(request)
        }

        binding.tvBtnDirectOpenwa.setOnClickListener {
            openWa(this,"+6282112966360", "Hi admin, Saya ingin mendafatar akun")
        }
    }

    override fun onClickBtnLoginView() {
        binding.progressBar.visibility = View.VISIBLE
        btnLogin.visibility = View.GONE
    }

    override fun onResponseErrorView() {
        binding.progressBar.visibility = View.GONE
        btnLogin.visibility = View.VISIBLE
    }

    override fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showResponseMessageSuccess(data: UserResponse.SingleResponse) {
        Paper.book().write(ID, data.data._id)
        Paper.book().write(USERNAME, data.data.username)
        Paper.book().write(FULLNAME, data.data.fullName)
        Paper.book().write(EMAIL, data.data.email)
        Paper.book().write(TOKEN, data.data.jwt_token)
        Paper.book().write(PROFILE_PHOTO, data.data.profile_image)
        Paper.book().write(STORAGE_PATH_PROFILE, data.data.path_storage)
        Paper.book().write(DIVISION, data.data.position)
    }

    override fun showResponseMessageError(msg: String) {
        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Uppss!")
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton("Retry") {_, _ ->}
            .show()
    }

    override fun onInputUsernameErrorView() {
        binding.outlinedTextFieldEmail.requestFocus()
        binding.outlinedTextFieldEmail.helperText = "Username is required!"
        binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
        binding.progressBar.visibility = View.GONE
        btnLogin.visibility = View.VISIBLE
    }

    override fun onInputPasswordErrorView() {
        binding.outlinedTextFieldPassword.requestFocus()
        binding.outlinedTextFieldPassword.helperText = "Password is required!"
        binding.outlinedTextFieldPassword.setHelperTextColor(getColorStateList(R.color.red_smooth))
        binding.progressBar.visibility = View.GONE
        btnLogin.visibility = View.VISIBLE
    }

    override fun onSuccessValidationSignIn(userRequest: UserRequest) {
        presenter.signInUser(userRequest)
    }

    private fun textWatcher(input: EditText) = object : android.text.TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(input) {
                binding.etEmail -> {
                    if (text.isNotEmpty()) {
                        binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                        binding.outlinedTextFieldEmail.isHelperTextEnabled = false
                        binding.outlinedTextFieldPassword.isEnabled = true
                    } else {
                        binding.outlinedTextFieldPassword.isEnabled = false
                        binding.outlinedTextFieldEmail.helperText = "Username gaboleh kosong!"
                        binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
                        binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
                binding.etPassword -> {
                    btnLogin.isEnabled = text.isNotEmpty()
                }
            }
        }

    }

}