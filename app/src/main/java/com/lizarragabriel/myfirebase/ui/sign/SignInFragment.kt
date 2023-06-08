package com.lizarragabriel.myfirebase.ui.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.databinding.FragmentSignInBinding
import com.lizarragabriel.myfirebase.utils.setupSignFinish
import com.lizarragabriel.myfirebase.utils.setupSnackbar
import com.lizarragabriel.myfirebase.utils.signFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val myModel: SignViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myToolbar()
        binding.apply {
            model = myModel
            lifecycleOwner = viewLifecycleOwner
        }
        myOnFinish()
        mySnackbar()
    }

    private fun myToolbar() {
        binding.mToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun mySnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, myModel.snackbar, Snackbar.LENGTH_SHORT)
    }

    private fun myOnFinish() {
        view?.setupSignFinish(binding.mEmail, binding.mPassword, binding.mCPassword, viewLifecycleOwner, myModel.finish)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}