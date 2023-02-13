package com.example.warehouseproject.core.view.main.account_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        // Set TopBar
        topAppBar()

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