package com.example.entryticketsales.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.entryticketsales.network.Api
import com.example.entryticketsales.network.RequestBuilder
import com.example.entryticketsales.utils.AppConstants
import com.example.entryticketsales.vos.BlogModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class EntryTicketsInfoViewModel(application: Application): AndroidViewModel(application){

    private val network = RequestBuilder.createService(Api::class.java)
    val url     = AppConstants.BASE_URL + "?key=" + AppConstants.KEY
    var result = MutableLiveData<BlogModel>()

    fun getTicketInfo() {
        network.getTicketInfo(url).enqueue((object : Callback<BlogModel> {

                override fun onFailure(call: Call<BlogModel>, t: Throwable) {
                    Timber.e(t)
                }
                override fun onResponse(call: Call<BlogModel>, response: Response<BlogModel>) {
                    if (response.isSuccessful) {
                        result.value = response.body()
                    }
                }
            }))
    }
}