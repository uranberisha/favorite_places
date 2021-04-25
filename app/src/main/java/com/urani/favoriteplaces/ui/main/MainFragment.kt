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
import com.urani.favoriteplaces.ui.main.adapter.PlacesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), OnMapReadyCallback{

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    val zoomLevel = 13.0f
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var mContext: Context

    private lateinit var placesAdapter: PlacesAdapter

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

        placesAdapter =
            PlacesAdapter(mContext, ArrayList()) { callback ->
                zoomMapsToInitialState(LatLng(callback.latitude, callback.latitude))
            }

        binding.groupsRecyclerView.adapter = placesAdapter

        //iewModel.deleteAllPlaces()

        observerLiveData()

        return binding.root
    }

    private fun observerLiveData() {
        viewModel.getAllFavoritePlaces().observe(viewLifecycleOwner, Observer { lisOffPlaces ->
            if (lisOffPlaces.isNullOrEmpty()) {
                placesAdapter.onAddPlaces(ArrayList())
            } else {
                placesAdapter.onAddPlaces(lisOffPlaces as MutableList<Place>)
            }
        })

    }



    fun onLogOutButtonClick(view: View?) {

    }

    fun onAddPostButtonClick(view: View?) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction().add(R.id.fragment_container_layout, AddPlaceFragment())
            .addToBackStack(null).commit();
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        binding.mapProgressBar.visibility = View.GONE
        map?.let {
            googleMap = map
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(42.6697152, 21.1440927),
                    zoomLevel
                )
            )


        }
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