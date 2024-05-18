package com.example.blinkitclone

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blinkitclone.databinding.LoadingLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Utils {
    private var alertDialog: AlertDialog? = null
    private var authInstance: FirebaseAuth? = null

    // Show alert dialog with given message
    fun showDialog(context: Context, message: String) {
        val layout = LoadingLayoutBinding.inflate(LayoutInflater.from(context))
        layout.tvProgressText.text = message
        alertDialog = MaterialAlertDialogBuilder(context).setView(layout.root).setCancelable(false).create()
        alertDialog?.show()
    }

    // Hide alert dialog
    fun hideDialog() {
        alertDialog?.dismiss()
    }

    // Show toast with given message
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Get firebase auth instance
    fun getAuthInstance(): FirebaseAuth {
        if (authInstance == null) {
            authInstance = Firebase.auth
        }
        return authInstance!!
    }

    // Get user id
    fun getUserId(): String {
        return Firebase.auth.currentUser!!.uid
    }

    // Get database instance
    fun getDatabaseRef(): DatabaseReference {
        return Firebase.database.reference
    }
}