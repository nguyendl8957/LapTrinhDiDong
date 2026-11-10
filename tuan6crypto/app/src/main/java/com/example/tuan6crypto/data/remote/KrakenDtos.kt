package com.example.tuan6crypto.data.remote

import com.google.gson.annotations.SerializedName

data class KrakenTickerResponse(
    val error: List<String> = emptyList(),
    val result: Map<String, KrakenTickerDto> = emptyMap()
)

data class KrakenTickerDto(
    @SerializedName("c")
    val lastTrade: List<String> = emptyList(),

    @SerializedName("o")
    val openingPrice: String = ""
)