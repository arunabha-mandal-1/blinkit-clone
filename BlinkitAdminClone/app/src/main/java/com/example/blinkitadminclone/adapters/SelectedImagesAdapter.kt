package com.example.blinkitadminclone.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkitadminclone.databinding.SelectImageItemBinding

class SelectedImagesAdapter(private val imageUris: ArrayList<Uri>) :
    RecyclerView.Adapter<SelectedImagesAdapter.SelectImagesViewHolder>() {

    inner class SelectImagesViewHolder(val binding: SelectImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectImagesViewHolder {
        return SelectImagesViewHolder(
            SelectImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: SelectImagesViewHolder, position: Int) {
        val currImageUri = imageUris[position]
        holder.binding.apply {
            ivProduct.setImageURI(currImageUri)

            // Remove image from the particular position
            ivDelete.setOnClickListener {
                if (position < imageUris.size) {
                    imageUris.removeAt(position)
                    notifyItemRemoved(position) // notify recyclerview that the image is removed from the particular position
//                    notifyItemRangeChanged(0, imageUris.size - 1)
                    notifyDataSetChanged() // must otherwise runtime err
                }
            }
        }

    }
}