package com.example.warehouseproject.core.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.awesomedialog.*
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.SavedPreferenceUser
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.paperdb.Paper
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

class SignInActivity : AppCompatActivity(), SignInView {

    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
    }

    private lateinit var binding: ActivitySignInBinding
    private lateinit var presenter: SignInPresenter
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Paper.init(this)

        id = Paper.book().read<String>(ID).toString()

        presenter = SignInPresenter(this, UserApiService(), SignInInteractor())

        binding.btnSubmitLogin.setOnClickListener {
            val request = UserRequest(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            presenter.validateFormSignIn(request)
        }

    }

    override fun onClickBtnLoginView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmitLogin.visibility = View.GONE
    }

    override fun onResponseErrorView() {
        binding.progressBar.visibility = View.GONE
        binding.btnSubmitLogin.visibility = View.VISIBLE
    }

    override fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showResponseMessageSuccess(data: UserResponse.SignIn) {
        Paper.book().write(ID, data.id)
        Paper.book().write(USERNAME, data.username)
        Paper.book().write(EMAIL, data.email)
        Paper.book().write(TOKEN, data.accessToken)
    }

    override fun showResponseMessageError(msg: String) {
        AwesomeDialog.build(this)
            .title("Upps!", null, resources.getColor(R.color.red_smooth))
            .body(msg, null, R.color.black)
            .position(AwesomeDialog.POSITIONS.BOTTOM)
            .onNegative("Close")
    }

    override fun onInputUsernameErrorView() {
        binding.outlinedTextFieldEmail.requestFocus()
        binding.outlinedTextFieldEmail.helperText = "Username is required!"
        binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
        binding.progressBar.visibility = View.GONE
        binding.btnSubmitLogin.visibility = View.VISIBLE
    }

    override fun onInputPasswordErrorView() {
        binding.outlinedTextFieldPassword.requestFocus()
        binding.outlinedTextFieldPassword.helperText = "Password is required!"
        binding.outlinedTextFieldPassword.setHelperTextColor(getColorStateList(R.color.red_smooth))
        binding.progressBar.visibility = View.GONE
        binding.btnSubmitLogin.visibility = View.VISIBLE
    }

    override fun onSuccessValidationSignIn(userRequest: UserRequest) {
        presenter.signInUser(userRequest)
    }

    override fun onStart() {
        super.onStart()

        if (id != "null") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}