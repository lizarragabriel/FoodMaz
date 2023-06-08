package com.lizarragabriel.myfirebase.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lizarragabriel.myfirebase.R
import com.lizarragabriel.myfirebase.databinding.FragmentDetailBinding
import com.lizarragabriel.myfirebase.utils.Val
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val myModel: DetailViewModel by viewModels()
    private val myRestaurant = Val.myRestaurant

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            model = myModel
            lifecycleOwner = viewLifecycleOwner
        }
        myToolbar()
        val myAdapter = ReviewAdapter()
        binding.reviewRecycler.adapter = myAdapter

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.mWeb.setOnClickListener {
            val myWeb = myRestaurant.contacto!!.web
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(myWeb))
            startActivity(intent)
        }
        binding.mCall.setOnClickListener {
            val phone = myRestaurant.contacto!!.tel
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat = myRestaurant.geo!!.latitude
        val lon = myRestaurant.geo!!.longitude
        val name = myRestaurant.name

        val sydney = LatLng(lat!!, lon!!)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title(name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

    private fun myToolbar() {
        binding.toolbar.title = myRestaurant.name
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}