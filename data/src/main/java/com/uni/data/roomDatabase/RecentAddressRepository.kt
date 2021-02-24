package com.uni.data.roomDatabase
import com.uni.data.roomDatabase.RecentAddress
import com.uni.data.roomDatabase.RecentAddressDao

import androidx.annotation.WorkerThread

class RecentAddressRepository(private val recentAddressDao: RecentAddressDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allRecentAddresses: List<RecentAddress> = recentAddressDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recentAddress: RecentAddress) {
        recentAddressDao.insert(recentAddress)
    }
}
