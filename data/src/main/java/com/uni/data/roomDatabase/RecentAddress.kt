package com.uni.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "recent_address_table")
class RecentAddress(

        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "address")
        val address: String,

        @ColumnInfo(name = "lat")
        val lat: Double,

        @ColumnInfo(name = "lng")
        val lng: Double,

        @ColumnInfo(name = "date")
        val date: String

)
