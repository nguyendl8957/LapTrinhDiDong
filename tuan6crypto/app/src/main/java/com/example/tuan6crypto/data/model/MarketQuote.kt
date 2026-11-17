package com.example.tuan6crypto.data.model

enum class PriceSource {
    CRYPTO_COMPARE,
    KRAKEN,
    BINANCE,
    COIN_GECKO
}

enum class TimeKind {
    PRICE_UPDATED,
    WINDOW_END,
    RECEIVED_ONLY
}

data class MarketQuote(
    val coin: CoinType,
    val source: PriceSource,
    val priceUsd: Double,
    val priceUpdatedAtMillis: Long?,
    val receivedAtMillis: Long,
    val change24hPercent: Double?,
    val timeKind: TimeKind,
    val currency: String = "USD"
)