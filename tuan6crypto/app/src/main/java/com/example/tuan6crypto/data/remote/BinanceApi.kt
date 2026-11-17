package com.example.tuan6crypto.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class BinanceTickerDto(
    val symbol: String = "",
    val lastPrice: String = "",
    val priceChangePercent: String = "",
    val closeTime: Long = 0L
)

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTickers(
        @Query("symbols") symbols: String
    ): List<BinanceTickerDto>
}
