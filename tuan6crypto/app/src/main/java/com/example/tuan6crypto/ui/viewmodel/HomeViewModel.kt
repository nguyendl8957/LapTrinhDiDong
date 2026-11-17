package com.example.tuan6crypto.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuan6crypto.data.model.*
import com.example.tuan6crypto.data.repository.CryptoRepository
import com.example.tuan6crypto.data.repository.FreshestQuoteSelector
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CryptoUiState(
    val coins: List<CoinUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val updatedAtMillis: Long? = null
)

class HomeViewModel : ViewModel() {

    private val repository = CryptoRepository()

    private val _uiState = MutableStateFlow(CryptoUiState())
    val uiState = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    private var chartJob: Job? = null

    // Cache các kết quả đã nhận từ bốn API.
    private var quotes: List<MarketQuote> = emptyList()

    private var ethHistory: List<Double> = emptyList()
    private var btcHistory: List<Double> = emptyList()

    fun startPolling() {
        if (pollingJob?.isActive == true) return

        startChartPolling()

        pollingJob = viewModelScope.launch {

            supervisorScope {

                // Bốn coroutine được khởi chạy độc lập.
                PriceSource.entries.forEach { source ->

                    launch {
                        while (isActive) {
                            try {
                                val result = when (source) {
                                    PriceSource.KRAKEN ->
                                        repository.fetchKraken()

                                    PriceSource.BINANCE ->
                                        repository.fetchBinance()

                                    PriceSource.CRYPTO_COMPARE ->
                                        repository.fetchCryptoCompare()

                                    PriceSource.COIN_GECKO ->
                                        repository.fetchCoinGecko()
                                }

                                // Kết quả nào về trước sẽ được
                                // xử lý trước, không chờ API còn lại.
                                publishQuotes(source, result)

                                Log.d(
                                    "CRYPTO_SOURCE",
                                    "$source OK: ${result.size} coins"
                                )

                            } catch (e: CancellationException) {
                                throw e

                            } catch (e: Exception) {
                                Log.e(
                                    "CRYPTO_SOURCE",
                                    "$source FAILED: ${e.message}",
                                    e
                                )

                                // Lỗi một nguồn không làm dừng
                                // ba nguồn còn lại.
                                refreshVisibleCoins()
                            }

                            // Nguồn giao dịch cập nhật nhanh;
                            // hai nguồn tổng hợp gọi chậm hơn
                            // để tránh tiêu thụ hạn mức quá nhiều.
                            val interval = when (source) {
                                PriceSource.KRAKEN,
                                PriceSource.BINANCE -> 5_000L

                                PriceSource.CRYPTO_COMPARE,
                                PriceSource.COIN_GECKO -> 60_000L
                            }

                            delay(interval)
                        }
                    }
                }
            }
        }
    }

    private fun publishQuotes(
        source: PriceSource,
        incoming: List<MarketQuote>
    ) {
        // Thay dữ liệu cũ của đúng nguồn vừa trả về.
        quotes = quotes.filterNot {
            it.source == source
        } + incoming

        refreshVisibleCoins()
    }

    private fun historyFor(coin: CoinType): List<Double> {
        val history = when (coin) {
            CoinType.ETH -> ethHistory
            CoinType.BTC -> btcHistory
            else -> emptyList()
        }

        if (history.isEmpty()) return emptyList()

        // OHLC lấy từ Kraken nên điểm cuối cũng dùng
        // giá Kraken, không nối lẫn giá sàn khác.
        val krakenPrice = quotes.firstOrNull {
            it.coin == coin &&
                    it.source == PriceSource.KRAKEN &&
                    System.currentTimeMillis() -
                    it.receivedAtMillis <= 120_000L
        }?.priceUsd

        return if (krakenPrice != null) {
            history + krakenPrice
        } else {
            history
        }
    }

    private fun refreshVisibleCoins() {
        val now = System.currentTimeMillis()

        val coins = CoinType.entries.map { type ->

            val selected = FreshestQuoteSelector.select(
                coin = type,
                quotes = quotes,
                nowMillis = now
            )

            Log.d(
                "CRYPTO_WINNER",
                "${type.symbol} | " +
                        "source=${selected?.source} | " +
                        "price=${selected?.priceUsd} | " +
                        "timestamp=${selected?.priceUpdatedAtMillis} | " +
                        "kind=${selected?.timeKind}"
            )

            CoinUiModel(
                type = type,
                priceUsd = selected?.priceUsd,
                changePercent = selected?.change24hPercent,
                priceHistory = historyFor(type),
                source = selected?.source,
                currency = selected?.currency ?: "USD"
            )
        }

        val hasPrices = coins.any {
            it.priceUsd != null
        }

        _uiState.value = CryptoUiState(
            coins = coins,
            isLoading = !hasPrices,
            error = if (hasPrices) null
            else "No valid market data",
            updatedAtMillis = now
        )
    }

    private fun startChartPolling() {
        if (chartJob?.isActive == true) return

        chartJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val (eth, btc) = supervisorScope {
                        val ethRequest = async {
                            repository.getPriceHistory("ETHUSD")
                        }

                        val btcRequest = async {
                            repository.getPriceHistory("XBTUSD")
                        }

                        ethRequest.await() to btcRequest.await()
                    }

                    ethHistory = eth
                    btcHistory = btc

                    refreshVisibleCoins()

                    Log.d(
                        "CRYPTO_CHART",
                        "ETH/BTC history loaded"
                    )

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    Log.e(
                        "CRYPTO_CHART",
                        "OHLC failed",
                        e
                    )
                }

                delay(60_000L)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null

        chartJob?.cancel()
        chartJob = null
    }
}