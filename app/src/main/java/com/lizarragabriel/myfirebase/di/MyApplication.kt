package com.lizarragabriel.myfirebase.di

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.utils.SharedPref
import com.lizarragabriel.myfirebase.utils.Val
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}