package com.uni.customer.features.address.selectAddress

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.customer.common.BaseViewModel
import com.uni.customer.common.getCurrentDateInServerFormat
import com.uni.customer.common.isStatusSuccess
import com.uni.data.models.SavedAddress
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException

class SelectAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsSavedAddressList: MutableLiveData<List<SavedAddress>> = MutableLiveData()

    init {
        getSavedAddress()
    }

    fun getSavedAddress() {

        ioScope.launch {
            try {
                obsSavedAddressList.postValue(repoSavedAddress.allWords)
                Log.e("SAVED ADDRESSES::", repoSavedAddress.allWords.toString())
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsSavedAddressList.postValue(null)
            }
        }
    }

    fun setSavedAddress(place: Place){
        ioScope.launch {
            repoSavedAddress.insert(SavedAddress(place.name, place.address, place.latLng, getCurrentDateInServerFormat()))
        }
    }

}