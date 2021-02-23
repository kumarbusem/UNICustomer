package com.uni.data.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentAddressDao {

    @Query("SELECT * FROM recent_address_table ORDER BY date DESC")
    fun getAlphabetizedWords(): List<RecentAddress>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recentAddress: RecentAddress)

    @Query("DELETE FROM recent_address_table")
    suspend fun deleteAll()
}
