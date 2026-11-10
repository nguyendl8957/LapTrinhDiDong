package com.example.tuan6crypto.data.model

import androidx.annotation.DrawableRes
import com.example.tuan6crypto.R

enum class CoinType(
    val symbol: String,
    val displayName: String,
    @DrawableRes val iconRes: Int
) {
    ETH("ETH", "Ethereum", R.drawable.ic_eth),
    BTC("BTC", "Bitcoin", R.drawable.ic_btc),
    LTC("LTC", "Litecoin", R.drawable.ic_ltc),
    XMR("XMR", "Monero", R.drawable.ic_xmr)
}

data class CoinUiModel(
    val type: CoinType,
    val priceUsd: Double? = null,
    val changePercent: Double? = null,
    val priceHistory: List<Double> = emptyList()
)