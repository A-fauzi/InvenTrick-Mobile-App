package com.example.warehouseproject.example_mvp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.warehouseproject.R
import com.example.warehouseproject.databinding.ActivityLoginBinding
import com.example.warehouseproject.example_mvp.main.ExampleMainActivity

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this, LoginInteractor())

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            validateCheckCredential()
        }

    }

    private fun validateCheckCredential() {
        presenter.validateCredential(binding.username.text.toString(), binding.password.text.toString())
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun setUsernameError() {
        binding.username.error = getString(R.string.username_error)
    }

    override fun setPasswordError() {
        binding.password.error = getString(R.string.password_error)
    }

    override fun navigateToHome() {
        startActivity(Intent(this, ExampleMainActivity::class.java))
    }
}