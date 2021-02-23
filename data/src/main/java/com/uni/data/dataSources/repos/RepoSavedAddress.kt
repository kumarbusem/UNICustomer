package com.uni.data.dataSources.repos

import androidx.annotation.WorkerThread
import com.uni.data.dataSources.roomDatabase.SavedAddressDao
import com.uni.data.models.SavedAddress
import kotlinx.coroutines.flow.Flow

class RepoSavedAddress(private val savedAddressDao: SavedAddressDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: List<SavedAddress> = savedAddressDao.getAllSavedAddresses()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(savedAddress: SavedAddress) {
        savedAddressDao.insert(savedAddress)
    }
}
