package com.lizarragabriel.myfirebase.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPref {

    private const val SESSION = "session"
    private const val SHAREDNAME = "mysharedpref"
    private lateinit var shared: SharedPreferences

    fun init(context: Context) {
        shared = context.getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE)
    }

    fun addSession(value: Boolean) {
        shared.edit().putBoolean(SESSION, value).apply()
    }

    fun getSession() = shared.getBoolean(SESSION, false)
}