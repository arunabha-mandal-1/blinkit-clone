package com.example.blinkitclone.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.blinkitclone.Utils
import com.example.blinkitclone.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow<Boolean>(false)
    private val _isSignedInSuccessfully = MutableStateFlow<Boolean>(false)
    private val _isCurrentUser = MutableStateFlow<Boolean>(false)

    val otpSent: StateFlow<Boolean> = _otpSent
    val isSignedInSuccessfully: StateFlow<Boolean> = _isSignedInSuccessfully
    val isCurrentUser: StateFlow<Boolean> = _isCurrentUser

    // Send OTP to a particular phone number
    fun sendOTP(userNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {}

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Sign in
    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: User) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                user.uid = Utils.getUserId()
                if (task.isSuccessful) {
                    _isSignedInSuccessfully.value = true

//                    _isCurrentUser.value = true // not here

                    // On successful sign in, store basic user information
                    Utils.getDatabaseRef().child("AllUsers").child("Users").child(user.uid!!).setValue(user)
                } else {
                    // ...
                }
            }
    }

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }

}