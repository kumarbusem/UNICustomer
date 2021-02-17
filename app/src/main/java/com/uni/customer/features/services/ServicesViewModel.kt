package com.uni.customer.features.services

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.uni.customer.common.BaseViewModel

class ServicesViewModel(context: Application) : BaseViewModel(context) {


    val obsIsUserAuthenticated = MutableLiveData<Boolean>()


}