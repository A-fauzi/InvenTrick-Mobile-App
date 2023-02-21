package com.example.warehouseproject.core.view.main.account_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.core.helper.SavedPreferenceUser
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.main.MainActivityPresenter
import com.example.warehouseproject.core.view.main.MainView
import com.example.warehouseproject.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class AccountFragment : Fragment(), MainView {
    companion object {
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
    }

    private lateinit var binding: FragmentAccountBinding

    private lateinit var presenter: MainActivityPresenter

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        presenter = MainActivityPresenter(requireContext(), requireActivity(), this, UserApiService())

        // Set TopBar
        topAppBar()

        Paper.init(requireContext())


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        binding.newTxtTopbar.viewEnd.setOnClickListener {

            val data = UserRequest.StatusActivity("offline")
            val token = Paper.book().read<String>(TOKEN).toString()
            val userId = Paper.book().read<String>(ID).toString()
            presenter.updateStatusActivityUser(token, userId, data)

            Paper.book().destroy()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()

//            mGoogleSignInClient.signOut().addOnCompleteListener {
////                val intent= Intent(requireContext(), SignInActivity::class.java)
////                startActivity(intent)
////                activity?.finish()
//
//
//            }
        }

        return binding.root
    }

    private fun topAppBar() {
        binding.newTxtTopbar.viewEnd.setImageResource(R.drawable.ic_settings)
        binding.newTxtTopbar.txtTopBar.text = getString(R.string.txt_topbar_account)

        binding.newTxtTopbar.viewEnd.setOnClickListener {
            binding.newTxtTopbar.viewEnd.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_button))
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }

        binding.newTxtTopbar.viewStart.setOnClickListener {
            binding.newTxtTopbar.viewStart.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_button))
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessBodyReqStatusView(response: UserResponse.SingleResponse) {
        Toast.makeText(requireContext(), "status anda ${response.data.status_activity}", Toast.LENGTH_SHORT).show()
    }

    override fun onErrorBodyReqStatusView(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureView(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}