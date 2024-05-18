package com.example.blinkitclone.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitclone.R
import com.example.blinkitclone.Utils
import com.example.blinkitclone.activity.UsersMainActivity
import com.example.blinkitclone.databinding.FragmentOTPBinding
import com.example.blinkitclone.models.User
import com.example.blinkitclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater, container, false)


        getUserNumber()
        sendOTP()
        onLoginButtonClick()
        customizeEnteringOtp()
        onBackButtonClicked()


        return binding.root
    }

    // Get user number coming from signin fragment
    private fun getUserNumber() {
        val args = arguments
        userNumber = args?.getString("number").toString()
        binding.tvUserNumber.text = userNumber
    }

    // Send otp to the given mobile number
    private fun sendOTP() {

        // Set in sign in fragment
//        Utils.showToast(requireContext(), "Sending OTP...")

        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect {
                    if (it) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "OTP sent!")
                    }
                }
            }
        }
    }

    private fun onLoginButtonClick() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing you...")
            val editTexts = arrayOf(
                binding.etOtp1,
                binding.etOtp2,
                binding.etOtp3,
                binding.etOtp4,
                binding.etOtp5,
                binding.etOtp6
            )
            val otp = editTexts.joinToString("") { it.text.toString() }
            if (otp.length < editTexts.size) {
                Utils.showToast(requireContext(), "Please enter right OTP")
                Utils.hideDialog()
            } else {
                editTexts.forEach {
                    it.text?.clear()
//                    it.clearFocus()
                }
                editTexts[0].requestFocus()
                verifyOTP(otp)
            }
        }
    }

    // Verify the OTP
    private fun verifyOTP(otp: String) {
        val user = User(uid = null, userPhoneNumber = userNumber, userAddress = "Delhi")
        viewModel.signInWithPhoneAuthCredential(otp, userNumber, user)
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect {
                if (it) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Signed in!")

                    // Go to users main activity after successful login
                    startActivity(Intent(requireContext(), UsersMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    // Customizing input functionality of 6 digits OTP
    private fun customizeEnteringOtp() {
        val editTexts = arrayOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }

            })
        }
    }

    // Handle left arrow click in toolbar
    private fun onBackButtonClicked() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }
}

