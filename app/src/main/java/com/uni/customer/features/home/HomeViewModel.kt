package com.uni.customer.features.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.MonthYear
import com.uni.data.models.Runsheet
import com.uni.data.models.User
import com.uni.customer.common.BaseViewModel
import com.uni.customer.common.isStatusSuccess
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.*

class HomeViewModel(context: Application) : BaseViewModel(context) {

    val obsUser: MutableLiveData<User> = MutableLiveData()
    val obsRunsheetssList: MutableLiveData<List<Runsheet>> = MutableLiveData()
    val obsMonthYear: MutableLiveData<MonthYear> = MutableLiveData()

}