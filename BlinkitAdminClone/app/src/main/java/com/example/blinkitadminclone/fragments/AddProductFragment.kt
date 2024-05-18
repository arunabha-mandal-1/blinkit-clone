package com.example.blinkitadminclone.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkitadminclone.Constants
import com.example.blinkitadminclone.Utils
import com.example.blinkitadminclone.adapters.SelectedImagesAdapter
import com.example.blinkitadminclone.databinding.FragmentAddProductBinding
import com.example.blinkitadminclone.models.Product
import com.example.blinkitadminclone.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val imagesUris: ArrayList<Uri> = arrayListOf()
    private val selectImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { listOfUris ->
            val fiveImages = listOfUris.take(5)
            imagesUris.clear()
            imagesUris.addAll(fiveImages)
            binding.prodImagesRecyclerView.adapter = SelectedImagesAdapter(imagesUris)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)


        setAutoCompleteTextView()
        onImageSelectClick()
        onAddBtnClick()


        return binding.root
    }


    // Set adapters of all the AutoCompleteTexViews
    private fun setAutoCompleteTextView() {
        val productCategoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            Constants.allProductsCategory
        )
        val productTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.allProductType
        )
        val productUnitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.allUnitsOfProducts
        )

        binding.apply {
            actProductCategory.setAdapter(productCategoryAdapter)
            actProductType.setAdapter(productTypeAdapter)
            actProductUnit.setAdapter(productUnitAdapter)
        }
    }


    // Select 5 images from gallery
    private fun onImageSelectClick() {
        binding.selectImages.setOnClickListener {
            selectImages.launch("image/*")
        }
    }

    // Add product(s)
    private fun onAddBtnClick() {
        binding.btnAddProduct.setOnClickListener {

            Utils.showDialog(requireContext(), "Uploading image...")

            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.actProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductNoStock.text.toString()
            val productCategory = binding.actProductCategory.text.toString()
            val productType = binding.actProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() ||
                productPrice.isEmpty() || productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()
            ) {
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Empty fields are not allowed!")
                }
            } else if (imagesUris.isEmpty()) {
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please upload some product images!")
                }
            } else {
                val product = Product(
                    productRandomId = Utils.getRandomId(),
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getUserId()
                )
                saveImages(product)
            }
        }
    }

    // Upload product images
    private fun saveImages(product: Product) {
        viewModel.saveImagesToDb(imagesUris)
        lifecycleScope.launch {
            viewModel.isImageUploaded.collect {
                if (it) {
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(), "Image Saved!")
                    }
                    getImageUrls(product)
                }
//                else {
//                    Utils.apply {
//                        hideDialog()
//                        showToast(requireContext(), "Failed to upload product image!")
//                    }
//                }
            }
        }
    }

    // Get uploaded product images
    private fun getImageUrls(product: Product) {
        Utils.showDialog(requireContext(), "Publishing product...")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect {
                val urls = it
                product.productImageUris = urls
                saveProduct(product)
            }
        }
    }

    // Finally save product in the db
    private fun saveProduct(product: Product) {
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect {
                    if (it) {
                        Utils.apply {
                            hideDialog()
                            showToast(requireContext(), "Product published!")
                        }
                    }
//                else {
//                    Utils.apply {
//                        hideDialog()
//                        showToast(requireContext(), "Failed to publish product!")
//                    }
//                }
                }
        }
    }
}