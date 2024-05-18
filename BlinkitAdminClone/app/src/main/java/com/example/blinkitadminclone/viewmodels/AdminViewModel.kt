package com.example.blinkitadminclone.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.blinkitadminclone.Utils
import com.example.blinkitadminclone.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class AdminViewModel : ViewModel() {

    private val _isImageUploaded = MutableStateFlow(false)
    private val _downloadedUrls = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    private val _isProductSaved = MutableStateFlow(false)


    val isImageUploaded: StateFlow<Boolean> = _isImageUploaded
    val downloadedUrls: StateFlow<ArrayList<String?>> = _downloadedUrls
    val isProductSaved: StateFlow<Boolean> = _isProductSaved


    init {
        _isImageUploaded.value = false
        _downloadedUrls.value = arrayListOf()
        _isProductSaved.value = false
    }


    // Upload product images to db
    fun saveImagesToDb(imageUris: ArrayList<Uri>) {
        val downloadUrls = arrayListOf<String?>()

        imageUris.forEach { uri ->
            val imageRef = Utils.getStorageRef().child(Utils.getUserId()).child("productImages")
                .child(UUID.randomUUID().toString())
            imageRef.putFile(uri).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                val url = task.result
                downloadUrls.add(url.toString())

                if (downloadUrls.size == imageUris.size) {
                    _isImageUploaded.value = true
                    _downloadedUrls.value = downloadUrls
                }
            }
        }
    }

    // Save product in db
    fun saveProduct(product: Product) {
        Utils.getDatabaseRef()
            .child("AllUsers/Admins/AdminProducts/${Utils.getUserId()}/Products/${product.productRandomId}")
            .setValue(product) // for particular admin/shop owner, this one is extra
            .addOnSuccessListener {
                Utils.getDatabaseRef()
                    .child("AllUsers/Admins/AllProducts/${product.productRandomId}")
                    .setValue(product) // all the products
                    .addOnSuccessListener {
                        Utils.getDatabaseRef()
                            .child("AllUsers/Admins/ProductsByCategory/${product.productRandomId}")
                            .setValue(product) // by their category
                            .addOnSuccessListener {
                                Utils.getDatabaseRef()
                                    .child("AllUsers/Admins/ProductsByType/${product.productRandomId}")
                                    .setValue(product) // by their type
                                    .addOnSuccessListener {
                                        _isProductSaved.value = true
                                    }
                            }
                    }
            }
    }
}