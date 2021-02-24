package com.uni.customer.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import com.uni.customer.R
import com.uni.customer.common.*
import com.uni.customer.databinding.FragmentHomeBinding
import com.uni.data.roomDatabase.RecentAddress
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.io.IOException
import java.util.*

class HomeFragment : BaseAbstractFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@HomeFragment) }
    private var pickupMarker: Marker? = null
    private var destinationMarker: Marker? = null
    override fun setViewModel(): HomeViewModel =
            ViewModelProvider(this@HomeFragment, ViewModelFactory {
                HomeViewModel(requireActivity().application)
            }).get(HomeViewModel::class.java)

    override fun setupViews(): FragmentHomeBinding.() -> Unit = {

        Log.e("SetUpViews::", "Entered")

        toggleBottomBarVisibility(true)
        setUpMap()

        pickupCard.setOnClickListener {
            setSelectAddreddFor(SELECT_ADDRESS_FOR_PICKUP)
            navigateById(R.id.action_homeFragment_to_selectAddressFragment)
        }
        destinationCard.setOnClickListener {
            setSelectAddreddFor(SELECT_ADDRESS_FOR_DESTINATION)
            navigateById(R.id.action_homeFragment_to_selectAddressFragment)
        }
    }

    private fun setUpMap() {
        var mMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    override fun setupObservers(): HomeViewModel.() -> Unit = {

    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            googleMap.clear()
            doBasicThings(googleMap)
            pickupMarker?.remove()
            destinationMarker?.remove()
            getPickupAddress { pickupPlace ->
                if (pickupPlace == null) {
                    initFreshMap(googleMap)
                } else {
                    initPickupMap(googleMap, pickupPlace)
                    getDestinationAddress { destinationPlace ->
                        if (destinationPlace != null) {
                            initDestinationMap(googleMap, pickupPlace, destinationPlace)
                        }
                    }
                }
            }
        }
    }

    private fun initDestinationMap(googleMap: GoogleMap, pickupPlace: RecentAddress?, destinationPlace: RecentAddress?) {
        mBinding.cvFindTrucks.show()
        destinationMarker = googleMap.addMarker(MarkerOptions()
                .position(destinationPlace?.getLatlngsFromRecentAddress()!!)
                .title("End Point")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.orange_marker)))
        mBinding.destinationText.text = "${destinationPlace.name}, ${destinationPlace.address}"
        loadRoute(googleMap, pickupPlace, destinationPlace)
    }

    private fun initFreshMap(googleMap: GoogleMap?) {
        showMylocationOnCamera(googleMap)
        googleMap?.setOnCameraIdleListener {
            getAddressFromLocation(googleMap.cameraPosition.target)
        }
    }

    private fun initPickupMap(googleMap: GoogleMap?, place: RecentAddress?) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(place?.getLatlngsFromRecentAddress()!!, 18.toFloat()))
        googleMap?.setOnCameraIdleListener {}
        mBinding.ivPickupCentreMarker.hide()
        pickupMarker = googleMap?.addMarker(MarkerOptions()
                .position(place?.getLatlngsFromRecentAddress()!!)
                .title("Start Point")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.green_marker)))
        mBinding.pickupText.text = "${place?.name}, ${place?.address}"
    }

    private fun doBasicThings(googleMap: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.isMyLocationEnabled = false
        googleMap?.uiSettings?.isCompassEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isZoomControlsEnabled = false
        googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json))

        mBinding.btnMyLocation.setOnClickListener {
            showMylocationOnCamera(googleMap)
            googleMap.isMyLocationEnabled = true
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        Log.e("ON MAP Ready", "333")
        val geocoder = Geocoder(requireContext(), Locale.ENGLISH)
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty()!!) {
                val address = addresses[0].getAddressLine(0).toString()
                mBinding.pickupText.text = address

                setPickupAddress(RecentAddress(address.substring(0, address.indexOf(",")),
                        address.substring(address.indexOf(",") + 1, address.length), latLng.latitude,
                        latLng.longitude, getCurrentDateInServerFormat()))

            } else {
                mBinding.pickupText.text = "Searching Current Address"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ERROR::", e.printStackTrace().toString())
        }
    }

    private fun showMylocationOnCamera(googleMap: GoogleMap?) {
        getLocation(requireContext()) {
            if (it != null)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16.toFloat()))
        }
    }

    private fun loadRoute(mMap: GoogleMap?, pickupPlace: RecentAddress?, destinationPlace: RecentAddress?) {

        val barcelona = pickupPlace?.getLatlngsFromRecentAddress()
        val madrid = destinationPlace?.getLatlngsFromRecentAddress()

        //Define list to get all latlng for the route

        //Define list to get all latlng for the route
        val path: MutableList<LatLng> = ArrayList()

        //Execute Directions API request

        //Execute Directions API request
        val context: GeoApiContext = GeoApiContext.Builder()
                .apiKey("AIzaSyBxuxXzQw5ZKY1KXEKFiZGh_JRBHZGS1ro")
                .build()

        val req: DirectionsApiRequest = DirectionsApi.getDirections(context, "${barcelona?.latitude},${barcelona?.longitude}",
                "${madrid?.latitude},${madrid?.longitude}")
        try {
            val res: DirectionsResult = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.isNotEmpty()) {
                val route: DirectionsRoute = res.routes.get(0)
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg: DirectionsLeg = route.legs.get(i)
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step: DirectionsStep = leg.steps.get(j)
                                if (step.steps != null && step.steps.isNotEmpty()) {
                                    for (k in step.steps.indices) {
                                        val step1: DirectionsStep = step.steps.get(k)
                                        val points1: EncodedPolyline = step1.polyline
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            val coords1: MutableList<com.google.maps.model.LatLng>? = points1.decodePath()
                                            for (coord1 in coords1!!) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points: EncodedPolyline = step.polyline
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        val coords: MutableList<com.google.maps.model.LatLng>? = points.decodePath()
                                        for (coord in coords!!) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.localizedMessage)
        }

        //Draw the polyline

        //Draw the polyline
        if (path.size > 0) {
            val opts = PolylineOptions().addAll(path).color(Color.BLACK).width(10f)
            mMap?.addPolyline(opts)
        }

        val bc = LatLngBounds.Builder().include(pickupPlace?.getLatlngsFromRecentAddress()).include(destinationPlace?.getLatlngsFromRecentAddress())
        mMap?.setPadding(0, 0, 0, 0)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(
                bc.build(),
                (this.resources.displayMetrics.widthPixels * 0.9).toInt(),
                (this.resources.displayMetrics.heightPixels * 0.5).toInt(),
                100))
    }

    override fun onResume() {
        initLoation(requireActivity(), requireContext())
        super.onResume()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        Log.e("MARKER CLICK", marker.toString())
        if(marker == pickupMarker){
            setSelectAddreddFor(SELECT_ADDRESS_FOR_PICKUP)
            navigateById(R.id.action_homeFragment_to_selectAddressFragment)
        }else if(marker== destinationMarker){
                setSelectAddreddFor(SELECT_ADDRESS_FOR_DESTINATION)
                navigateById(R.id.action_homeFragment_to_selectAddressFragment)
        }
        return true
    }
}