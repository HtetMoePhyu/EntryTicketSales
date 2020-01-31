package com.example.entryticketsales.network

import com.example.entryticketsales.vos.BlogModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET
    fun getTicketInfo(@Url url:String): Call<BlogModel>
}