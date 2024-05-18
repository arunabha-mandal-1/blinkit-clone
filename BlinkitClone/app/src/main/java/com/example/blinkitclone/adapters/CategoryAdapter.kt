package com.example.blinkitclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blinkitclone.databinding.CategoryItemBinding
import com.example.blinkitclone.models.Category

class CategoryAdapter(val categoryList: List<Category>) :
    Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: CategoryItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            category.image?.let { ivCategoryImage.setImageResource(it) }
            tvCategoryName.text = category.title
        }
    }
}