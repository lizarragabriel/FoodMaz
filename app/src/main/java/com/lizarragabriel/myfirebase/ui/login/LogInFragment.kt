package com.lizarragabriel.myfirebase.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.databinding.FragmentLogInBinding
import com.lizarragabriel.myfirebase.utils.SharedPref
import com.lizarragabriel.myfirebase.utils.myNavigate
import com.lizarragabriel.myfirebase.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private val myModel: LoginViewModel by viewModels()
    private var auth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCheckSession()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            frag = this@LogInFragment
            model = myModel
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.signInButton.setOnClickListener { googleButton() }
        mySnackbar()
        myNavigate()
    }

    private fun mCheckSession() {
        val mySession = SharedPref.getSession()
        if(mySession) {
            val myUser = auth.currentUser
            if(myUser != null) {
                mUpdateUI()
            }
        }
    }

    fun signIn() {
        view!!.findNavController().navigate(R.id.action_logInFragment_to_signInFragment)
    }

    fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile", "user_friends"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object  : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    result.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                setSession()
                                mUpdateUI()
                            }
                        }
                    }
                }

                override fun onCancel() {
                    println("onCancel")
                }

                override fun onError(error: FacebookException) {
                    println("onerror. ${error.message}")
                }
            })
    }

    private fun mySnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, myModel.snackbar, Snackbar.LENGTH_SHORT)
    }

    private fun myNavigate() {
        view?.myNavigate(viewLifecycleOwner, myModel.updateUI)
    }

    private fun googleButton() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("ONRESULT. $requestCode")
        callbackManager.onActivityResult(requestCode, resultCode, data)
        // callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    setSession()
                    mUpdateUI()
                }
            }
    }

    private fun setSession() {
        SharedPref.addSession(true)
    }


    private fun mUpdateUI() {
        binding.myProgress.visibility = View.GONE
        view!!.findNavController().navigate(R.id.action_logInFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}