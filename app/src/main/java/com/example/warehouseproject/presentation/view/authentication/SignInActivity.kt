package com.example.warehouseproject.presentation.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant.User.DIVISION
import com.example.warehouseproject.core.constant.Constant.User.EMAIL
import com.example.warehouseproject.core.constant.Constant.User.FULLNAME
import com.example.warehouseproject.core.constant.Constant.User.ID
import com.example.warehouseproject.core.constant.Constant.User.PROFILE_PHOTO
import com.example.warehouseproject.core.constant.Constant.User.STORAGE_PATH_PROFILE
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.core.constant.Constant.User.USERNAME
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.utils.helper.ZiHelper.openWa
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.data.repositoryimpl.user.AuthRepository
import com.example.warehouseproject.databinding.ActivitySignInBinding
import com.example.warehouseproject.presentation.viewmodels.auth.AuthViewModel
import com.example.warehouseproject.presentation.viewmodels.auth.AuthViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import io.paperdb.Paper

// SignInView
class SignInActivity : AppCompatActivity() {

    private lateinit var authViewModelProvider: AuthViewModelProvider
    private lateinit var authRequestModel: UserAuthRequestModel
    private lateinit var authRepository: AuthRepository
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var btnLogin: Button
    private var id = "null"


    private fun initView() {
        btnLogin = binding.btnComponentLogin.btnComponent
        btnLogin.text = getString(R.string.login)

        Paper.init(this)
        id = Paper.book().read<String>(ID).toString()

        authRepository = AuthRepository()
        authViewModelProvider = AuthViewModelProvider(authRepository)
        viewModel = ViewModelProvider(this, authViewModelProvider)[AuthViewModel::class.java]

        checkCurrentUser()
        Picasso.get()
            .load("https://i.pinimg.com/originals/ed/0a/a7/ed0aa728d861d69cdce28fb3055f9fd9.gif")
            .into(binding.ivContent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()

    }

    override fun onStart() {
        super.onStart()

        buttonAction()
        editTextSetTextWatcher(binding.etEmail)
        editTextSetTextWatcher(binding.etPassword)

    }


    /**
     * Fungsi checkCurrentUser digunakan untuk memeriksa apakah pengguna sudah login sebelumnya.
    Fungsi ini akan memeriksa nilai dari variabel id, jika nilainya bukan "null", maka aktivitas MainActivity akan dibuka dan aktivitas saat ini akan diakhiri.
     */
    private fun checkCurrentUser() {
        if (id != "null") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    /**
     * Fungsi editTextSetTextWatcher digunakan untuk menambahkan TextWatcher pada EditText etEmail dan etPassword di layout.
    TextWatcher digunakan untuk memantau perubahan pada teks yang dimasukkan pada EditText.
     */
    private fun editTextSetTextWatcher(editText: EditText) {

        editText.addTextChangedListener(SignInInteractor().textWatcher(editText, object :
            SignInInteractor.OnFinishedTextWatcher {

            override fun onInputTextWatcher(text: String, input: EditText) {
                when (input) {
                    binding.etEmail -> {
                        if (text.isNotEmpty()) {
                            binding.outlinedTextFieldEmail.endIconMode =
                                TextInputLayout.END_ICON_CUSTOM
                            binding.outlinedTextFieldEmail.isHelperTextEnabled = false
                            binding.outlinedTextFieldPassword.isEnabled = true
                        } else {
                            binding.outlinedTextFieldPassword.isEnabled = false
                            binding.outlinedTextFieldEmail.helperText = "Username gaboleh kosong!"
                            binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
                            binding.outlinedTextFieldEmail.endIconMode =
                                TextInputLayout.END_ICON_NONE
                        }
                    }

                    binding.etPassword -> {
                        btnLogin.isEnabled = text.isNotEmpty()
                    }
                }
            }

        }))
    }


    /**
     * Fungsi buttonAction digunakan untuk menambahkan aksi pada tombol login dan tombol tvBtnDirectOpenwa.
    Jika tombol login ditekan, maka akan dibuat sebuah objek request dari UserRequest dengan username dan password yang diambil dari EditText etEmail dan etPassword, kemudian akan memanggil presenter.validateFormSignIn dengan parameter request.
    Jika tombol tvBtnDirectOpenwa ditekan, maka akan memanggil fungsi openWa dengan parameter nomor telepon admin dan pesan yang ingin dikirimkan.
     */
    private fun buttonAction() {

        btnLogin.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE

            authRequestModel = UserAuthRequestModel(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
            viewModel.login(authRequestModel)
        }

        binding.tvBtnDirectOpenwa.setOnClickListener {
            openWa(this, "+6282112966360", "Hi admin, Saya ingin mendafatar akun")
        }

    }

    private fun setupViewModel() {
        viewModel.authResult.observe(this) { authResult ->
            when (authResult) {
                is AuthViewModel.AuthResult.Success -> {
                    Paper.book().write(ID, authResult.response.data._id)
                    Paper.book().write(USERNAME, authResult.response.data.username)
                    Paper.book().write(FULLNAME, authResult.response.data.fullName)
                    Paper.book().write(EMAIL, authResult.response.data.email)
                    Paper.book().write(TOKEN, authResult.response.data.jwt_token)
                    Paper.book().write(PROFILE_PHOTO, authResult.response.data.profile_image)
                    Paper.book().write(STORAGE_PATH_PROFILE, authResult.response.data.path_storage)
                    Paper.book().write(DIVISION, authResult.response.data.position)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is AuthViewModel.AuthResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    btnLogin.visibility = View.VISIBLE
                    MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle("Uppss!")
                        .setMessage(authResult.msg)
                        .setCancelable(true)
                        .setPositiveButton("Retry") { _, _ -> }
                        .show()
                }

                is AuthViewModel.AuthResult.Failure -> {
                    MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle("Uppss!")
                        .setMessage("Ada kesalahan, Tidak dapat terhubung ke jaringan!")
                        .setCancelable(true)
                        .setPositiveButton("Retry") { _, _ -> }
                        .show()
                }
            }
        }
    }
}