package com.lizarragabriel.myfirebase.ui.sign

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.repository.Repository
import com.lizarragabriel.myfirebase.utils.MyEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var cpassword: String

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _snackbar = MutableLiveData<MyEvent<Int>>()
    val snackbar: LiveData<MyEvent<Int>> get() = _snackbar

    private var _finish = MutableLiveData<MyEvent<Boolean>>()
    val finish: LiveData<MyEvent<Boolean>> get() = _finish

    fun signButton(email: String, password: String, cpassword: String) {
        if(email.isEmpty()) {

            mySnackbar(R.string.sign_empty_email)
            return
        }
        if(password.isEmpty()) {
            mySnackbar(R.string.sign_empty_password)
            return
        }
        if(cpassword.isEmpty()) {
            mySnackbar(R.string.sign_empty_cpassword)
            return
        }
        if(password != cpassword) {
            mySnackbar(R.string.sign_no_match)
            return
        }
        _loading.postValue(true)

        this.email = email
        this.password = password
        this.cpassword = cpassword

        createUser()
    }

    private fun createUser() {
        try {
            repository.createUser(email, password)
                .addOnSuccessListener {
                    val myUs = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("https://firebase.google.com/images/brand-guidelines/logo-logomark.png?hl=es-419")).build()
                    it.user?.updateProfile(myUs)
                    addUser()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.message)
                    when(it) {
                        is FirebaseNetworkException -> mySnackbar(R.string.no_network)
                        is FirebaseAuthInvalidCredentialsException -> mySnackbar(R.string.user_nofind)
                        is FirebaseAuthWeakPasswordException -> mySnackbar(R.string.low_password)
                    }
                }
                .addOnCompleteListener {
                    _loading.postValue(false)
                }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun addUser() {
        val myMap = hashMapOf(
            "email" to email,
            "password" to password,
            "time" to FieldValue.serverTimestamp()
        )
        repository
            .addUser(myMap)
            .addOnSuccessListener {
                mySnackbar(R.string.user_added)
                _finish.value = MyEvent(true)
            }
            .addOnFailureListener { println("failure") }
            .addOnCompleteListener { println("complete") }
    }

    private fun mySnackbar(message: Int) {
        _snackbar.value = MyEvent(message)
    }
}