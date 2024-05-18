package com.example.blinkitadminclone.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadminclone.R
import com.example.blinkitadminclone.activity.AdminMainActivity
import com.example.blinkitadminclone.databinding.FragmentSplashBinding
import com.example.blinkitadminclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewModel.isCurrentUser.collect {
                    if (it) {
                        startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                        requireActivity().finish()
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                    }
                }
            }
        }, 2000)


        return binding.root
    }
}