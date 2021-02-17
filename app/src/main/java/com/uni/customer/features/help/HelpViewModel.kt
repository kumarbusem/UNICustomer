package com.uni.customer.features.help

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.uni.customer.common.BaseViewModel

class HelpViewModel(context: Application) : BaseViewModel(context) {


    val obsIsUserAuthenticated = MutableLiveData<Boolean>()


}