package com.example.blinkitclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blinkitclone.adapters.CategoryAdapter
import com.example.blinkitclone.databinding.FragmentHomeBinding
import com.example.blinkitclone.models.Category

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        setAllCategories()


        return binding.root
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()
        for(i in 0 until Constants.allProductsCategory.size){
            categoryList.add(Category(Constants.allProductsCategory[i], Constants.allProductsCategoryIcons[i]))
        }
        binding.rvShopByCategory.adapter = CategoryAdapter(categoryList)
    }
}