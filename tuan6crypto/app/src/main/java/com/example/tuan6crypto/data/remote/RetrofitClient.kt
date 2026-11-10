package com.example.tuan6crypto.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.kraken.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: KrakenApi =
        retrofit.create(KrakenApi::class.java)
}