package com.example.tuan6crypto.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class CoinGeckoMarketDto(
    val id: String = "",
    @SerializedName("current_price")
    val currentPrice: Double? = null,
    @SerializedName("price_change_percentage_24h")
    val change24h: Double? = null,
    @SerializedName("last_updated")
    val lastUpdated: String? = null
)

interface CoinGeckoApi {
    @GET("api/v3/coins/markets")
    suspend fun getMarkets(
        @Header("x-cg-demo-api-key") apiKey: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String =
            "bitcoin,ethereum,litecoin,monero"
    ): List<CoinGeckoMarketDto>
}