package com.uni.customer.features.address.selectAddress

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.uni.customer.common.BaseViewModel
import com.uni.data.roomDatabse.Word
import kotlinx.coroutines.launch

class SelectAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsSavedAddressList: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        getSavedAddress()
    }

    fun getSavedAddress() {

        ioScope.launch {
            try {
                val allWords: List<Word> = repository.allWords
                allWords.forEach {
                    Log.e("WORDS::", it.word.toString())
                }
                obsSavedAddressList.postValue(allWords)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsSavedAddressList.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun setSavedAddress(place: Place){
        ioScope.launch {
            repository.insert(Word(place.name.toString()))
        }
    }

}