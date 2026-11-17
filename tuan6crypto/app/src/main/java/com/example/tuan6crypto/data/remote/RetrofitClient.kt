package com.example.tuan6crypto.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    val api: KrakenApi = createRetrofit(
        "https://api.kraken.com/"
    ).create(KrakenApi::class.java)

    val binanceApi: BinanceApi = createRetrofit(
        "https://data-api.binance.vision/"
    ).create(BinanceApi::class.java)

    val cryptoCompareApi: CryptoCompareApi = createRetrofit(
        "https://min-api.cryptocompare.com/"
    ).create(CryptoCompareApi::class.java)

    val coinGeckoApi: CoinGeckoApi = createRetrofit(
        "https://api.coingecko.com/"
    ).create(CoinGeckoApi::class.java)
}