package com.example.tuan6crypto.data.repository

import com.example.tuan6crypto.BuildConfig
import com.example.tuan6crypto.data.model.*
import com.example.tuan6crypto.data.remote.RetrofitClient
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.Instant

class CryptoRepository {

    private fun typeOf(symbol: String): CoinType? =
        CoinType.entries.firstOrNull {
            it.symbol == symbol
        }

    private fun JsonObject.number(key: String): Double? =
        runCatching {
            get(key)?.takeUnless { it.isJsonNull }?.asDouble
        }.getOrNull()

    private fun JsonObject.longValue(key: String): Long? =
        runCatching {
            get(key)?.takeUnless { it.isJsonNull }?.asLong
        }.getOrNull()

    suspend fun fetchKraken(): List<MarketQuote> {
        val response = RetrofitClient.api.getTicker(
            "XBTUSD,ETHUSD,LTCUSD,XMRUSD"
        )

        if (response.error.isNotEmpty()) {
            error(response.error.joinToString())
        }

        val keys = mapOf(
            CoinType.ETH to "XETHZUSD",
            CoinType.BTC to "XXBTZUSD",
            CoinType.LTC to "XLTCZUSD",
            CoinType.XMR to "XXMRZUSD"
        )

        val received = System.currentTimeMillis()

        return keys.mapNotNull { (coin, key) ->
            val ticker = response.result[key]
                ?: return@mapNotNull null

            val price = ticker.lastTrade.firstOrNull()
                ?.toDoubleOrNull()
                ?: return@mapNotNull null

            val open = ticker.openingPrice.toDoubleOrNull()
                ?: return@mapNotNull null

            if (!price.isFinite() || price <= 0 ||
                !open.isFinite() || open <= 0
            ) return@mapNotNull null

            MarketQuote(
                coin = coin,
                source = PriceSource.KRAKEN,
                priceUsd = price,
                priceUpdatedAtMillis = null,
                receivedAtMillis = received,
                change24hPercent =
                    (price - open) / open * 100,
                timeKind = TimeKind.RECEIVED_ONLY
            )
        }
    }

    suspend fun fetchBinance(): List<MarketQuote> {
        val response = RetrofitClient.binanceApi
            .getTickers(
                """["BTCUSDT","ETHUSDT","LTCUSDT"]"""
            )

        val received = System.currentTimeMillis()

        return response.mapNotNull { ticker ->
            val symbol = ticker.symbol.removeSuffix("USDT")
            val coin = typeOf(symbol)
                ?: return@mapNotNull null

            val price = ticker.lastPrice.toDoubleOrNull()
                ?: return@mapNotNull null

            if (!price.isFinite() || price <= 0) {
                return@mapNotNull null
            }

            MarketQuote(
                coin = coin,
                source = PriceSource.BINANCE,
                priceUsd = price,
                priceUpdatedAtMillis =
                    ticker.closeTime.takeIf { it > 0 },
                receivedAtMillis = received,
                change24hPercent = ticker
                    .priceChangePercent.toDoubleOrNull(),
                timeKind = TimeKind.WINDOW_END,
                currency = "USDT"
            )
        }
    }

    suspend fun fetchCryptoCompare(): List<MarketQuote> {
        val key = BuildConfig.CRYPTOCOMPARE_KEY
        require(key.isNotBlank()) {
            "CryptoCompare API key chưa cấu hình"
        }

        val response = RetrofitClient.cryptoCompareApi
            .getPrices(
                symbols = "BTC,ETH,LTC,XMR",
                apiKey = key
            )

        val raw = response.getAsJsonObject("RAW")
            ?: error("CryptoCompare missing RAW")

        val received = System.currentTimeMillis()

        return CoinType.entries.mapNotNull { coin ->
            val data = raw.getAsJsonObject(coin.symbol)
                ?.getAsJsonObject("USD")
                ?: return@mapNotNull null

            val price = data.number("PRICE")
                ?: return@mapNotNull null

            if (!price.isFinite() || price <= 0) {
                return@mapNotNull null
            }

            MarketQuote(
                coin = coin,
                source = PriceSource.CRYPTO_COMPARE,
                priceUsd = price,
                priceUpdatedAtMillis = data
                    .longValue("LASTUPDATE")?.times(1000),
                receivedAtMillis = received,
                change24hPercent =
                    data.number("CHANGEPCT24HOUR"),
                timeKind = if (
                    data.longValue("LASTUPDATE") != null
                ) TimeKind.PRICE_UPDATED
                else TimeKind.RECEIVED_ONLY
            )
        }
    }

    suspend fun fetchCoinGecko(): List<MarketQuote> {
        val key = BuildConfig.COINGECKO_KEY
        require(key.isNotBlank()) {
            "CoinGecko API key chưa cấu hình"
        }

        val response = RetrofitClient.coinGeckoApi
            .getMarkets(apiKey = key)

        val received = System.currentTimeMillis()

        val coinIds = mapOf(
            "ethereum" to CoinType.ETH,
            "bitcoin" to CoinType.BTC,
            "litecoin" to CoinType.LTC,
            "monero" to CoinType.XMR
        )

        return response.mapNotNull { item ->
            val coin = coinIds[item.id]
                ?: return@mapNotNull null

            val price = item.currentPrice
                ?: return@mapNotNull null

            if (!price.isFinite() || price <= 0) {
                return@mapNotNull null
            }

            val updated = runCatching {
                Instant.parse(item.lastUpdated)
                    .toEpochMilli()
            }.getOrNull()

            MarketQuote(
                coin = coin,
                source = PriceSource.COIN_GECKO,
                priceUsd = price,
                priceUpdatedAtMillis = updated,
                receivedAtMillis = received,
                change24hPercent = item.change24h,
                timeKind = if (updated != null) {
                    TimeKind.PRICE_UPDATED
                } else {
                    TimeKind.RECEIVED_ONLY
                }
            )
        }
    }

    // Giữ tính năng biểu đồ thật từ Kraken OHLC.
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

        val candles: JsonArray = result.entrySet()
            .firstOrNull { it.key != "last" }
            ?.value
            ?.asJsonArray
            ?: error("Missing OHLC candles for $pair")

        return candles.toList()
            .dropLast(1)
            .takeLast(29)
            .map { candle ->
                candle.asJsonArray[4]
                    .asString.toDoubleOrNull()
                    ?: error("Invalid close price")
            }.also {
                require(it.size >= 2) {
                    "Not enough historical prices"
                }
            }
    }
}