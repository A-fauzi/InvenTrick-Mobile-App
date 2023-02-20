package com.example.warehouseproject.core.view.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
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

class SignInActivity : AppCompatActivity(), SignInView {
    companion object {
        private const val TAG = "SignInActivity"
        private const val GOOGLE_SIGNIN_REQ_CODE = 1046

        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
    }

    private lateinit var binding: ActivitySignInBinding

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var presenter: SignInPresenter

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)

        id = Paper.book().read<String>(ID).toString()

        auth = FirebaseAuth.getInstance()

        presenter = SignInPresenter(this, UserApiService())

        configureGso()

//        binding.btnSignGoogle.setOnClickListener {
//            signInGoogle()
//        }

        binding.btnSubmitLogin.setOnClickListener {
            val request = UserRequest(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            presenter.signInUser(request)
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

    override fun showResponseMessageSuccess(data: UserResponse.SingleResponse) {
        Toast.makeText(this, "Hallo ${data.username}", Toast.LENGTH_SHORT).show()

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

    private fun configureGso() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGNIN_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGNIN_REQ_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completeTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completeTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUi(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Google oAuth handle methode
     */
    private fun UpdateUi(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                SavedPreferenceUser.setEmail(this, account.email.toString())
                SavedPreferenceUser.setUsername(this, account.displayName.toString())
                SavedPreferenceUser.setPhoto(this, account.photoUrl.toString())
                SavedPreferenceUser.setUid(this, account.id.toString())

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
               Toast.makeText(this, "Task not successfuly", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { signInCredentialFailure ->
            Toast.makeText(this, signInCredentialFailure.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }

        if (id != "null") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}