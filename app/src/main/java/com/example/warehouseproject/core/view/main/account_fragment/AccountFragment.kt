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
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        // Set TopBar
        topAppBar()

        Paper.init(requireContext())


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        binding.newTxtTopbar.viewEnd.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
//                val intent= Intent(requireContext(), SignInActivity::class.java)
//                startActivity(intent)
//                activity?.finish()

                Paper.book().destroy()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                requireActivity().finish()
            }
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
}