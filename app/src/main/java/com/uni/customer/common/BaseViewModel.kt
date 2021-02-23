package com.uni.customer.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.uni.data.dataSources.definitions.DataSourceImage
import com.uni.data.dataSources.definitions.DataSourceSharedPreferences
import com.uni.data.dataSources.definitions.DataSourceTextData
import com.uni.data.dataSources.definitions.DataSourceUser
import com.uni.data.dataSources.repos.*
import com.uni.data.dataSources.roomDatabase.UniRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel(context: Application) : AndroidViewModel(context) {

    val obsIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val obsMessage = MutableLiveData<String>()
    var isUserLogout = MutableLiveData<Boolean>()

    protected val TAG: String = javaClass.simpleName

    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }
    protected val repoUser: DataSourceUser by lazy { RepoUser() }
    protected val repoTextData: DataSourceTextData by lazy { RepoTextData() }
    protected val repoImage: DataSourceImage by lazy { RepoImage() }
    val database by lazy { UniRoomDatabase.getDatabase() }
    val repoSavedAddress by lazy { RepoSavedAddress(database.savedAddressDao()) }


    override fun onCleared() {
        super.onCleared()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }
}