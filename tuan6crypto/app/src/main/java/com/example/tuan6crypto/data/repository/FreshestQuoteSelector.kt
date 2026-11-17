package com.example.tuan6crypto.data.repository

import com.example.tuan6crypto.data.model.*

object FreshestQuoteSelector {

    fun select(
        coin: CoinType,
        quotes: List<MarketQuote>,
        nowMillis: Long = System.currentTimeMillis()
    ): MarketQuote? {

        val valid = quotes.filter { q ->
            q.coin == coin &&
                    q.priceUsd.isFinite() &&
                    q.priceUsd > 0 &&
                    q.receivedAtMillis <= nowMillis + 10_000 &&
                    nowMillis - q.receivedAtMillis <= 120_000 &&
                    (
                            q.priceUpdatedAtMillis == null ||
                                    (
                                            q.priceUpdatedAtMillis <= nowMillis + 10_000 &&
                                                    nowMillis - q.priceUpdatedAtMillis <= 120_000
                                            )
                            )
        }

        // Chỉ so timestamp trong nhóm có cùng ý nghĩa.
        val updated = valid.filter {
            it.timeKind == TimeKind.PRICE_UPDATED &&
                    it.priceUpdatedAtMillis != null
        }

        if (updated.isNotEmpty()) {
            return updated.maxWithOrNull(
                compareBy<MarketQuote> {
                    it.priceUpdatedAtMillis ?: Long.MIN_VALUE
                }.thenBy {
                    -it.receivedAtMillis
                }
            )
        }

        // Không có timestamp giá đáng tin cậy:
        // dùng giá có mốc WINDOW_END nếu hợp lệ.
        val windowQuotes = valid.filter {
            it.timeKind == TimeKind.WINDOW_END &&
                    it.priceUpdatedAtMillis != null
        }

        if (windowQuotes.isNotEmpty()) {
            return windowQuotes.maxByOrNull {
                it.priceUpdatedAtMillis ?: Long.MIN_VALUE
            }
        }

        // Trường hợp dự phòng: nguồn nhận sớm nhất
        // trong nhóm dữ liệu còn hợp lệ.
        return valid.minByOrNull { it.receivedAtMillis }
    }
}