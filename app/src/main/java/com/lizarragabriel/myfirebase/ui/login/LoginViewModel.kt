package com.lizarragabriel.myfirebase.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.repository.Repository
import com.lizarragabriel.myfirebase.utils.MyEvent
import com.lizarragabriel.myfirebase.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private lateinit var email: String
    private lateinit var password: String

    // email layout
    private var _errorEmail = MutableLiveData<Boolean>()
    val errorEmail: LiveData<Boolean> get() = _errorEmail

    // password layout
    private var _errorPassword = MutableLiveData<Boolean>()
    val errorPassword: LiveData<Boolean> get() = _errorPassword

    // snackbar if find an error
    private var _snackbar = MutableLiveData<MyEvent<Int>>()
    val snackbar: LiveData<MyEvent<Int>> get() = _snackbar

    // set a navigation
    private var _updateUI = MutableLiveData<MyEvent<Int>>()
    val updateUI: LiveData<MyEvent<Int>> get() = _updateUI

    fun loginButton(email: String, password: String) {
        myError(_errorEmail, false)
        myError(_errorPassword, false)

        if(email.isEmpty()) {
            myError(_errorEmail, true)
            mySnackBar(R.string.empty_email)
            return
        }
        if(password.isEmpty()) {
            myError(_errorPassword, true)
            mySnackBar(R.string.empty_password)
            return
        }

        this.email = email
        this.password = password
        login()
    }

    private fun login() {
        val myTask = repository.login(email, password)
        myTask
            .addOnSuccessListener {
                setSession()
                myUpdateUI()
            }
            .addOnCompleteListener {
                if(it.exception != null) {
                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            mySnackBar(R.string.user_nofind)
                        }
                        is FirebaseNetworkException -> {
                            mySnackBar(R.string.no_network)
                        }
                    }
                }
            }
    }

    private fun myError(data: MutableLiveData<Boolean>, error: Boolean) {
        data.postValue(error)
    }

    private fun mySnackBar(message: Int) {
        _snackbar.value = MyEvent(message)
    }


    private fun myUpdateUI() {
        _updateUI.value = MyEvent(R.id.action_logInFragment_to_homeFragment)
    }

    private fun setSession() {
        SharedPref.addSession(true)
    }
}