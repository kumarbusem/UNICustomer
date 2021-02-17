package com.uni.customer.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import android.view.Display
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible


fun getRandomUDID(): String = UUID.randomUUID().toString()

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


fun isGpsAvailable(context: Context): Boolean {
    val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun String.isStatusSuccess(): Boolean {
    return this == "success"
}

fun getCurrentAppVersion(context: Context): String {
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, 0)
    return info.versionName
}

fun getCurrentHalfScreen(activity: Activity, d: Double): Int {
    val display: Display = activity.windowManager.defaultDisplay
    val width = display.width
    return (width / d).toInt()
}


val KProperty0<*>.isLazyInitialized: Boolean
    get() {
        // Prevent IllegalAccessException from JVM access check
        isAccessible = true
        return (getDelegate() as Lazy<*>).isInitialized()
    }

fun getCurrentDateInServerFormat(): String = getTimeInServerFormat()

fun getTimeInServerFormat(): String {

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return simpleDateFormat.format(Date())
}

@SuppressLint("MissingPermission")
fun initLoation(activity: Activity, context: Context){
    var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    var mLocationRequest = LocationRequest.create()
    mLocationRequest.interval = 60000
    mLocationRequest.fastestInterval = 5000
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                return
            }
            for (location in locationResult.locations) {
                if (location != null) {

                }
            }
        }
    }
    LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
}

@SuppressLint("MissingPermission")
fun getLocation(context: Context, res: (Location?) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context).lastLocation
            .addOnSuccessListener { location: Location? ->
                res(location)
            }.addOnFailureListener {
                res(null)
            }
}