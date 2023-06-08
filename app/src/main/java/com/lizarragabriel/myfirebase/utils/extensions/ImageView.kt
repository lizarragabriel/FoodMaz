package com.lizarragabriel.myfirebase.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String, circle: Boolean) {
    if(circle) {
        val hola = Glide.with(this).load(url).circleCrop().into(this)
    } else {
        Glide.with(this).load(url).into(this)
    }
}