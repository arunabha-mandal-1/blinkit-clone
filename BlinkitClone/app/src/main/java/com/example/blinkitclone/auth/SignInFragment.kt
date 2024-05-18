package com.example.blinkitclone.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blinkitclone.R
import com.example.blinkitclone.Utils
import com.example.blinkitclone.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)


        getUserNumber()
        onContinueButtonClick()

        return binding.root
    }

    private fun getUserNumber() {
        binding.etUserNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length
                if (len == 10) {
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                    binding.btnContinue.isClickable = true
                } else {
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grayish_blue))
                    binding.btnContinue.isClickable = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()
            val bundle = Bundle()
            bundle.putString("number", number)
            findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)

            // Navigate and then show alert dialog
            Utils.showDialog(requireContext(), "Sending OTP")
        }
    }
}