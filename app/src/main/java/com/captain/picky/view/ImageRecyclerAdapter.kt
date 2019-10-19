package com.captain.picky.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.captain.picky.R
import com.captain.picky.models.ImagesResponseModel

class ImageRecyclerAdapter(val onBindviewHolderCallback: Callback.onBindviewHolderCallback) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    private var imageList: List<ImagesResponseModel>? = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_image_layout,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {

        return imageList?.size ?: 0

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        onBindviewHolderCallback.onBindViewHolder(holder, position)

    }

    fun showAllImages(list: List<ImagesResponseModel>) {
        imageList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}