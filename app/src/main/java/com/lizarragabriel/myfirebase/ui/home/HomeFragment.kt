package com.lizarragabriel.myfirebase.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.databinding.FragmentHomeBinding
import com.lizarragabriel.myfirebase.utils.SharedPref
import com.lizarragabriel.myfirebase.utils.Val
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val myModel: HomeViewModel by viewModels()

    private var auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            model = myModel
            lifecycleOwner = viewLifecycleOwner
        }
        myToolbar()
        val myAdapter = MyAdapter()
        binding.mRecyclerView.adapter = myAdapter
        myAdapter.onItemDetail = {
            Val.myRestaurant = it
            view.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        myAdapter.onItemCard = {
            Val.myRestaurant = it
            view.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
    }

    private fun setSession() {
        SharedPref.addSession(false)
    }

    private fun myToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.myToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return inflater.inflate(R.menu.my_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.mySignOut -> {
                setSession()
                auth.signOut()
                requireActivity().onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}