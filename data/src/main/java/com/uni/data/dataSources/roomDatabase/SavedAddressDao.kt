package com.uni.data.dataSources.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uni.data.models.SavedAddress


@Dao
interface SavedAddressDao {

    @Query("SELECT * FROM saved_addresses ORDER BY date ASC")
    fun getAllSavedAddresses(): List<SavedAddress>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(savedAddress: SavedAddress)

    @Query("DELETE FROM saved_addresses")
    suspend fun deleteAll()

}