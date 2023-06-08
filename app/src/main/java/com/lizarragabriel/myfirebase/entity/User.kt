package com.lizarragabriel.myfirebase.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class User(
    val email: String? = null,
    val password: String? = null,
    val time: Timestamp? = null)
