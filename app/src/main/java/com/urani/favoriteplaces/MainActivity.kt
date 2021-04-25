package com.urani.favoriteplaces

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urani.favoriteplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnMapLongClickListener  {
    private lateinit var binding: ActivityMainBinding

    private lateinit var googleMap: GoogleMap
    val zoomLevel = 13.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mapFragment =
            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)

        val userId = intent.getStringExtra("user_id")
        val email = intent.getStringExtra("email")
    }

    override fun onMapReady(map: GoogleMap?) {
        binding.mapProgressBar.visibility = View.GONE
        map?.let {
            googleMap = map
            googleMap.setOnMapLongClickListener(this)
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            googleMap.uiSettings.isZoomControlsEnabled = true

            zoomMapsToInitialState(LatLng(42.6697152,21.1440927))


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

}