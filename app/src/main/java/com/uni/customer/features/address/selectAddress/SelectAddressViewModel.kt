package com.uni.customer.features.address.selectAddress

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.customer.common.BaseViewModel
import com.uni.customer.common.isStatusSuccess
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException

class SelectAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsIsRunsheetSaved: MutableLiveData<Boolean> = MutableLiveData()


}