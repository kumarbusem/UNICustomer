package com.uni.data.models

import com.google.android.gms.maps.model.LatLng


data class PlaceDetails(

        var name: String?,
        var address: String?,
        var latLng: LatLng?
)