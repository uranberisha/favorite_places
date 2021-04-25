package com.urani.favoriteplaces.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.database.entities.Place
import com.urani.favoriteplaces.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    val zoomLevel = 13.0f
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var mContext: Context

    lateinit var allFavoritePlaces: List<Place>

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.fragment = this

//        val place1 = Place(0,42.6697152, 21.1440927, "https://dev-youlo.lb.noexislab.cloud/images/35602364-6a1e-40de-9044-93664c4b0f52.jpeg")
//        viewModel.insert(place1)
//
//        val place2= Place(0,42.657152, 21.1340927, "https://dev-youlo.lb.noexislab.cloud/images/35602364-6a1e-40de-9044-93664c4b0f52.jpeg")
//        viewModel.insert(place2)

        viewModel.deleteAll()

        observerLiveData()

        return binding.root
    }

    private fun observerLiveData() {
        viewModel.getAllFavoritePlaces().observe(viewLifecycleOwner, Observer { lisOffPlaces ->
            val a = lisOffPlaces
        })

    }

    override fun onMapReady(map: GoogleMap?) {
        binding.mapProgressBar.visibility = View.GONE
        map?.let {
            googleMap = map
            googleMap.setOnMapLongClickListener(this)
            googleMap.uiSettings.isZoomControlsEnabled = true

            zoomMapsToInitialState(LatLng(42.6697152, 21.1440927))


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