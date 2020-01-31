package com.example.entryticketsales.network

import com.example.entryticketsales.utils.AppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestBuilder {
    private lateinit var retrofit: Retrofit
    private var builder = Retrofit.Builder()

    fun <S> createService(serviceClass: Class<S>): S {
        retrofit = builder
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(serviceClass)
    }
}