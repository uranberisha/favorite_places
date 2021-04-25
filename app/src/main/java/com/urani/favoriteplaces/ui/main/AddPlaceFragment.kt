package com.urani.favoriteplaces.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.FragmentAddPlaceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPlaceFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private lateinit var binding: FragmentAddPlaceBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    private lateinit var googleMap: GoogleMap
    val zoomLevel = 13.0f
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_place, container, false)
        binding.fragment = this


        return binding.root
    }

    fun onBackButtonClick(view: View?) {
        activity?.onBackPressed()
    }

    fun onEditPlaceButtonClick(view: View?) {

    }

    fun onTakePhotoClicked(view: View?) {

    }

    fun onPasteLinkClicked(view: View?) {

    }

    fun onDeleteButtonClick(view: View?) {

    }

    override fun onMapReady(map: GoogleMap?) {
        binding.mapProgressBar.visibility = View.GONE
        map?.let {
            googleMap = map
            googleMap.setOnMapLongClickListener(this)
            googleMap.uiSettings.isZoomControlsEnabled = true

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(42.6697152, 21.1440927),
                    zoomLevel
                )
            )


        }
    }


    override fun onMapLongClick(latLng: LatLng?) {
        googleMap.clear()
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.addMarker(MarkerOptions().position(latLng!!))
        zoomMapsToInitialState(latLng)
    }

    private fun zoomMapsToInitialState(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng))
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                zoomLevel
            )
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)
    }

}