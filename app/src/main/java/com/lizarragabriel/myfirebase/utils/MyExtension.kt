package com.lizarragabriel.myfirebase.utils

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    myEvent: LiveData<MyEvent<Int>>,
    duration: Int
) {
    myEvent.observe(
        lifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                showSnackbar(context.getString(it), duration)
            }
        }
    )
}

fun View.showSnackbar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun View.myNavigate(
    lifecycleOwner: LifecycleOwner,
    myEvent: LiveData<MyEvent<Int>>,
) {
    myEvent.observe(
        lifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                this.findNavController().navigate(it)
            }
        }
    )
}

fun View.setupSignFinish(
    email: EditText,
    password: EditText,
    cpassword: EditText,
    lifecycleOwner: LifecycleOwner,
    myEvent: LiveData<MyEvent<Boolean>>,
) {
    myEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let { ifFinish ->
            if(ifFinish) {
                signFinish(email, password, cpassword)
            }
        }
    })
}

fun View.signFinish(
    email: EditText,
    password: EditText,
    cpassword: EditText
) {
    email.onEditorAction(EditorInfo.IME_ACTION_DONE)
    password.onEditorAction(EditorInfo.IME_ACTION_DONE)
    cpassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
}



