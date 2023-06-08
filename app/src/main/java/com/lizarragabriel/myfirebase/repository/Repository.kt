package com.lizarragabriel.myfirebase.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.entity.Review
import javax.inject.Inject

// collections
private const val USERS = "users"
private const val RESTAURANTS = "restaurantes"
private const val REVIEWS = "reviews"

// fields
private const val RESTAURANT = "restaurant"

class Repository @Inject constructor() {

    private val myFirestore = Firebase.firestore
    private val myAuth = Firebase.auth

    fun createUser(email: String, password: String): Task<AuthResult> {
        return myAuth.createUserWithEmailAndPassword(email, password)
    }

    fun addUser(myMap: HashMap<String, Any>): Task<DocumentReference> {
        return myFirestore.collection(USERS).add(myMap)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return myAuth.signInWithEmailAndPassword(email, password)
    }

    fun loadRestaurants(): CollectionReference {
        return myFirestore.collection(RESTAURANTS)
    }

    fun loadReviews(restaurant: String): Query {
        return myFirestore.collection(REVIEWS).whereEqualTo(RESTAURANT, restaurant)
    }

    fun addReview(review: Review): Task<DocumentReference> {

        return myFirestore.collection(REVIEWS).add(myConvert(review))
    }

    fun updateReview(review: Review): Task<Void> {
        return myFirestore.collection(REVIEWS).document(review.id!!).set(myConvert(review))
    }

    private fun myConvert(review: Review): Any {
        return hashMapOf(
            "comentario" to review.comentario,
            "photo" to review.photo,
            "raiting" to review.raiting,
            "restaurant" to review.restaurant,
            "usuario" to review.usuario)
    }
}