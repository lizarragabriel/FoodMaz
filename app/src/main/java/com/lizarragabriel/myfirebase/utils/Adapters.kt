package com.lizarragabriel.myfirebase.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.entity.Restaurant
import com.lizarragabriel.myfirebase.entity.Review
import com.lizarragabriel.myfirebase.ui.detail.ReviewAdapter
import com.lizarragabriel.myfirebase.ui.home.MyAdapter

object Adapters {
    @BindingAdapter("loadData")
    @JvmStatic
    fun loadData(recyclerView: RecyclerView, data: List<Restaurant>?) {
        val myAdapter = recyclerView.adapter
        if(myAdapter is MyAdapter && data != null) {
            myAdapter.setList(data)
        }
    }

    @BindingAdapter("loadReview")
    @JvmStatic
    fun loadReview(recyclerView: RecyclerView, data: List<Review>?) {
        val myAdapter = recyclerView.adapter
        if(myAdapter is ReviewAdapter && data != null) {
            println("si hay data en reviews")
            myAdapter.setList(data)
        }
    }

    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(image: ImageView, url: String?) {
        if(url != null) {
            Glide.with(image.context).load(url).into(image)
        }
    }

    @BindingAdapter("loadRound")
    @JvmStatic
    fun loadRound(image: ImageView, url: String?) {
        if(url != null) {
            Glide.with(image.context).load(url).circleCrop().into(image)
        }
    }

    @BindingAdapter("checkLayoutEmail")
    @JvmStatic
    fun checkLayoutEmail(layout: TextInputLayout, error: Boolean) {
        if(error) {
            val myContext = layout.context
            val myMessage = myContext.getString(R.string.empty_email)
            layout.isErrorEnabled = true
            layout.error = myMessage
        } else {
            layout.isErrorEnabled = false
            layout.error = null
        }
    }

    @BindingAdapter("checkLayoutPassword")
    @JvmStatic
    fun checkLayoutPassword(layout: TextInputLayout, error: Boolean) {
        if(error) {
            val myContext = layout.context
            val myMessage = myContext.getString(R.string.empty_password)
            layout.isErrorEnabled = true
            layout.error = myMessage
        } else {
            layout.isErrorEnabled = false
            layout.error = null
        }
    }
}