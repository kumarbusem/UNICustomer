package com.uni.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.android.gms.maps.model.LatLng
import java.util.*

@Entity(tableName = "saved_addresses")
public class SavedAddress(

        @ColumnInfo(name = "name")
        var name: String?,

        @ColumnInfo(name = "address")
        var address: String?,

        @ColumnInfo(name = "latlng")
        var latLng: LatLng?,

        @ColumnInfo(name = "date")
        var date: String?

)