package com.uni.customer.features.address.selectAddress

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.uni.customer.common.BaseViewModel
import com.uni.customer.common.getCurrentDateInServerFormat
import com.uni.data.models.Runsheet
import com.uni.data.roomDatabase.RecentAddress
import kotlinx.coroutines.launch

class SelectAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsSavedAddressList: MutableLiveData<List<RecentAddress>> = MutableLiveData()
    val obsRunsheetssList: MutableLiveData<List<Runsheet>> = MutableLiveData()
    init {
        getSavedAddress()
    }

    fun getSavedAddress() {

        ioScope.launch {
            try {
                val allRecentAddresses: List<RecentAddress> = repository.allRecentAddresses
                allRecentAddresses.forEach {
                    Log.e("WORDS::", it.name.toString())
                }
                obsSavedAddressList.postValue(allRecentAddresses)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsSavedAddressList.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun setSavedAddress(place: Place){
        ioScope.launch {
            repository.insert(RecentAddress(place.name.toString(), place.address.toString(),
                    place.latLng?.latitude!!, place.latLng?.longitude!!, getCurrentDateInServerFormat()))
        }
    }

}