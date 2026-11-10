package com.example.tuan6crypto.data.repository

import com.example.tuan6crypto.data.model.CoinType
import com.example.tuan6crypto.data.model.CoinUiModel
import com.example.tuan6crypto.data.remote.RetrofitClient

import com.google.gson.JsonArray

class CryptoRepository {

    suspend fun getCoins(): List<CoinUiModel> {
        val response = RetrofitClient.api.getTicker(
            "XBTUSD,ETHUSD,LTCUSD,XMRUSD"
        )

        if (response.error.isNotEmpty()) {
            error(response.error.joinToString())
        }

        val pairKeys = listOf(
            CoinType.ETH to "XETHZUSD",
            CoinType.BTC to "XXBTZUSD",
            CoinType.LTC to "XLTCZUSD",
            CoinType.XMR to "XXMRZUSD"
        )

        return pairKeys.map { (type, key) ->
            val ticker = response.result[key]
                ?: error("Missing Kraken pair: $key")

            val currentPrice = ticker.lastTrade
                .firstOrNull()
                ?.toDoubleOrNull()
                ?: error("Invalid price: $key")

            val openingPrice = ticker.openingPrice
                .toDoubleOrNull()
                ?: error("Invalid opening price: $key")

            require(currentPrice.isFinite() && currentPrice >= 0.0)
            require(openingPrice.isFinite() && openingPrice > 0.0)

            val changePercent =
                (currentPrice - openingPrice) / openingPrice * 100.0

            CoinUiModel(
                type = type,
                priceUsd = currentPrice,
                changePercent = changePercent
            )
        }
    }


    suspend fun getPriceHistory(
        pair: String
    ): List<Double> {
        val response = RetrofitClient.api.getOhlc(
            pair = pair,
            interval = 1
        )

        val errors = response.getAsJsonArray("error")
        if (errors != null && errors.size() > 0) {
            error(errors.toString())
        }

        val result = response.getAsJsonObject("result")
            ?: error("Missing OHLC result")

        // Kraken có thể trả mã nội bộ của cặp giao dịch.
        val candles: JsonArray = result.entrySet()
            .firstOrNull { it.key != "last" }
            ?.value
            ?.asJsonArray
            ?: error("Missing OHLC candles for $pair")

        // Loại cây nến cuối vì nó chưa đóng.
        val completedCandles = candles
            .toList()
            .dropLast(1)
            .takeLast(29)

        val prices = completedCandles.map { candle ->
            val row = candle.asJsonArray

            row[4].asString.toDoubleOrNull()
                ?: error("Invalid OHLC close price")
        }

        require(prices.size >= 2) {
            "Not enough historical prices"
        }

        return prices
    }
}