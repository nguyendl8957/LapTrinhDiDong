package com.example.tuan6crypto.data.remote

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCompareApi {
    @GET("data/pricemultifull")
    suspend fun getPrices(
        @Query("fsyms") symbols: String,
        @Query("tsyms") currencies: String = "USD",
        @Query("api_key") apiKey: String
    ): JsonObject
}
