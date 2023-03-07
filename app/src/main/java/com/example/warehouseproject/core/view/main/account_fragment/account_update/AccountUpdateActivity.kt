package com.example.warehouseproject.core.view.main.account_fragment.account_update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.databinding.ActivityAccountUpdateBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topAppBar()

        val email = Paper.book().read<String>(EMAIL).toString()
        val username = Paper.book().read<String>(USERNAME).toString()
        val fullName = Paper.book().read<String>(FULLNAME).toString()
        val photo = Paper.book().read<String>(PROFILE_PHOTO).toString()
        val division = Paper.book().read<String>(DIVISION).toString()

        Picasso.get().load(photo).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivProfile)
        binding.tvProfileFullname.text = fullName
        binding.tvDivision.text = division
        binding.etEmail.setText(email)
        binding.etUsername.setText(username)
        binding.etFullname.setText(fullName)
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
}