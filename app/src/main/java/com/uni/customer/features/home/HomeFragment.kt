package com.uni.customer.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.uni.customer.R
import com.uni.customer.common.*
import com.uni.customer.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.io.IOException
import java.lang.String
import java.util.*

class HomeFragment : BaseAbstractFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {
    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@HomeFragment) }
    override fun setViewModel(): HomeViewModel =
            ViewModelProvider(this@HomeFragment, ViewModelFactory {
                HomeViewModel(requireActivity().application)
            }).get(HomeViewModel::class.java)

    override fun setupViews(): FragmentHomeBinding.() -> Unit = {

        Log.e("SetUpViews::", "Entered")

        toggleBottomBarVisibility(true)
        setUpMap()

        pickupCard.setOnClickListener { navigateById(R.id.action_homeFragment_to_selectAddressFragment) }
        destinationCard.setOnClickListener { navigateById(R.id.action_homeFragment_to_selectAddressFragment) }
    }

    private fun setUpMap() {
        var mMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    override fun setupObservers(): HomeViewModel.() -> Unit = {

    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isCompassEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json))
        getLocation(requireContext()){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it?.latitude!!, it.longitude), 14.7.toFloat()))
        }
        googleMap.setOnCameraIdleListener {
            getAddressFromLocation(googleMap.cameraPosition.target.latitude, googleMap.cameraPosition.target.longitude)
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.ENGLISH)
        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                val fetchedAddress: Address = addresses[0]
                Log.e("Address11::", fetchedAddress.getAddressLine(0).toString())
                mBinding.pickupText.text = fetchedAddress.getAddressLine(0).toString()
            } else {
                mBinding.pickupText.text = "Searching Current Address"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ERROR::", e.printStackTrace().toString())
        }
    }

    override fun onResume() {
        initLoation(requireActivity(), requireContext())
        super.onResume()
    }
}