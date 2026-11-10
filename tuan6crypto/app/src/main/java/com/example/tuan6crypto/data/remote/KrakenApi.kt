package com.example.tuan6crypto.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface KrakenApi {

    @GET("0/public/Ticker")
    suspend fun getTicker(
        @Query("pair") pair: String
    ): KrakenTickerResponse


    @GET("0/public/OHLC")
    suspend fun getOhlc(
        @Query("pair") pair: String,
        @Query("interval") interval: Int = 1
    ): com.google.gson.JsonObject

}