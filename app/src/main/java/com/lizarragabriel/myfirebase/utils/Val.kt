package com.lizarragabriel.myfirebase.utils

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.entity.Restaurant

object Val {
    fun myLog(message: String) = Log.d("FirebaseTest", message)

    lateinit var myRestaurant: Restaurant
}