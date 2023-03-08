package com.example.warehouseproject.core.view.main.account_fragment.account_update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.warehouseproject.R
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.databinding.ActivityAccountUpdateBinding
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class AccountUpdateActivity : AppCompatActivity() {
    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val FULLNAME = "fullname"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val STORAGE_PATH_PROFILE = "path"
        private const val PROFILE_PHOTO = "photo"
        private const val DIVISION = "division"
    }

    private lateinit var binding: ActivityAccountUpdateBinding

    private lateinit var etEmail: EditText
    private lateinit var etUserName: EditText
    private lateinit var etFullName: EditText
    private lateinit var etPassword: EditText

    private fun initView() {
        etEmail = binding.etEmail
        etUserName = binding.etUsername
        etFullName = binding.etFullname
        etPassword = binding.etPassword
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        topAppBar()

        val email = Paper.book().read<String>(EMAIL).toString()
        val username = Paper.book().read<String>(USERNAME).toString()
        val fullName = Paper.book().read<String>(FULLNAME).toString()
        val photo = Paper.book().read<String>(PROFILE_PHOTO).toString()
        val division = Paper.book().read<String>(DIVISION).toString()

        Picasso.get().load(photo).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivProfile)
        binding.tvProfileFullname.text = fullName
        binding.tvDivision.text = division
        etEmail.setText(email)
        etUserName.setText(username)
        etFullName.setText(fullName)

        etEmail.addTextChangedListener(textWatcher(etEmail))
        etUserName.addTextChangedListener(textWatcher(etUserName))
        etFullName.addTextChangedListener(textWatcher(etFullName))
        etPassword.addTextChangedListener(textWatcher(etPassword))
    }

    private fun topAppBar() {
        binding.newTxtTopbar.viewStart.visibility = View.GONE
        val arrowBackPress = binding.newTxtTopbar.viewStartArrowBack
        arrowBackPress.visibility = View.VISIBLE
        arrowBackPress.setOnClickListener {
            onBackPressed()
        }

        binding.newTxtTopbar.viewEnd.visibility = View.GONE
        binding.newTxtTopbar.txtTopBar.text = "Edit Profile"
        binding.newTxtTopbar.cvStatusActivityUser.visibility = View.GONE
        binding.newTxtTopbar.tvStatusActivityUser.visibility = View.GONE
    }

    private fun textWatcher(input: EditText) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(input) {
                 binding.etEmail -> {
                     if (text.isNotEmpty()) {
                         if (text.contains("@gmail.com")) {
                             binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                             binding.outlinedTextFieldEmail.isHelperTextEnabled = false
                             binding.outlinedTextFieldUsername.isEnabled = true
                         } else {
                             binding.outlinedTextFieldUsername.isEnabled = false
                             binding.outlinedTextFieldEmail.helperText = "Format email tidak valid! harus '@gmail.com' "
                             binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
                             binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_NONE
                         }
                     } else {
                         binding.outlinedTextFieldUsername.isEnabled = false
                         binding.outlinedTextFieldEmail.helperText = "Email gaboleh kosong!"
                         binding.outlinedTextFieldEmail.setHelperTextColor(getColorStateList(R.color.red_smooth))
                         binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_NONE
                     }
                }
                binding.etUsername -> {
                    if (text.length >= 3) {
                        binding.outlinedTextFieldUsername.endIconMode = TextInputLayout.END_ICON_CUSTOM
                        binding.outlinedTextFieldUsername.isHelperTextEnabled = false
                        binding.outlinedTextFieldFullname.isEnabled = true
                    } else {
                        binding.outlinedTextFieldUsername.endIconMode = TextInputLayout.END_ICON_NONE
                        binding.outlinedTextFieldUsername.isHelperTextEnabled = true
                        binding.outlinedTextFieldUsername.helperText = "Username max 3 character dan tidak boleh kosong!"
                        binding.outlinedTextFieldUsername.setHelperTextColor(getColorStateList(R.color.red_smooth))
                        binding.outlinedTextFieldFullname.isEnabled = false
                    }
                }
                binding.etFullname -> {
                    if (text.length >= 3) {
                        binding.outlinedTextFieldFullname.endIconMode = TextInputLayout.END_ICON_CUSTOM
                        binding.outlinedTextFieldFullname.isHelperTextEnabled = false
                        binding.outlinedTextFieldPassword.isEnabled = true
                    } else {
                        binding.outlinedTextFieldFullname.endIconMode = TextInputLayout.END_ICON_NONE
                        binding.outlinedTextFieldFullname.isHelperTextEnabled = true
                        binding.outlinedTextFieldFullname.helperText = "Full Name max 3 character dan tidak boleh kosong!"
                        binding.outlinedTextFieldFullname.setHelperTextColor(getColorStateList(R.color.red_smooth))
                        binding.outlinedTextFieldPassword.isEnabled = false
                    }
                }
                binding.etPassword -> {
                    if (text.length >= 8) {
                        binding.outlinedTextFieldPassword.setStartIconDrawable(R.drawable.ic_badge_check)
                        binding.btnSubmitUpdateUser.isEnabled = true
                        binding.outlinedTextFieldPassword.isHelperTextEnabled = false
                    } else {
                        binding.outlinedTextFieldPassword.setStartIconDrawable(0)
                        binding.btnSubmitUpdateUser.isEnabled = false
                        binding.outlinedTextFieldPassword.isHelperTextEnabled = true
                        binding.outlinedTextFieldPassword.helperText = "Password max 8 character dan tidak boleh kosong!"
                        binding.outlinedTextFieldPassword.setHelperTextColor(getColorStateList(R.color.red_smooth))
                    }
                }
            }
        }

    }
}