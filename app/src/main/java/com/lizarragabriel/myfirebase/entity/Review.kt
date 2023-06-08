package com.lizarragabriel.myfirebase.entity

import android.net.Uri

data class Review(
    var comentario: String? = null,
    var raiting: Int? = null,
    val restaurant: String? = null,
    val usuario: String? = null,
    var id: String? = null,
    var photo: String? = null
)
