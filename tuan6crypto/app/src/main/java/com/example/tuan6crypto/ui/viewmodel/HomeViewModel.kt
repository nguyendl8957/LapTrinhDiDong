package com.example.tuan6crypto.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuan6crypto.data.model.CoinUiModel
import com.example.tuan6crypto.data.repository.CryptoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

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

    private var ethHistory: List<Double> = emptyList()
    private var btcHistory: List<Double> = emptyList()

    fun startPolling() {
        if (pollingJob?.isActive == true) return

        startChartPolling()

        pollingJob = viewModelScope.launch {
            while (isActive) {
                try {

                    val coins = repository.getCoins().map { coin ->
                        val history = when (coin.type.symbol) {
                            "ETH" -> ethHistory
                            "BTC" -> btcHistory
                            else -> emptyList()
                        }

                        coin.copy(
                            priceHistory = if (
                                history.isNotEmpty() && coin.priceUsd != null
                            ) {
                                history + coin.priceUsd
                            } else {
                                history
                            }
                        )
                    }

                    _uiState.value = CryptoUiState(
                        coins = coins,
                        isLoading = false,
                        error = null,
                        updatedAtMillis = System.currentTimeMillis()
                    )

                    Log.d(
                        "CRYPTO_POLL",
                        "Updated: " + coins.joinToString {
                            "${it.type.symbol}=${it.priceUsd}"
                        }
                    )

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Network error"
                    )

                    Log.e("CRYPTO_POLL", "Update failed", e)
                }

                delay(5_000L)
            }
        }
    }


    private fun startChartPolling() {
        if (chartJob?.isActive == true) return

        chartJob = viewModelScope.launch {
            while (isActive) {
                try {
                    // Lấy lịch sử ETH và BTC đồng thời.
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

                    // Cập nhật biểu đồ ngay nếu đã có giá Ticker.
                    _uiState.value = _uiState.value.copy(
                        coins = _uiState.value.coins.map { coin ->
                            val history = when (coin.type.symbol) {
                                "ETH" -> ethHistory
                                "BTC" -> btcHistory
                                else -> emptyList()
                            }

                            coin.copy(
                                priceHistory = if (
                                    history.isNotEmpty() &&
                                    coin.priceUsd != null
                                ) {
                                    history + coin.priceUsd
                                } else {
                                    history
                                }
                            )
                        }
                    )

                    Log.d("CRYPTO_CHART", "ETH/BTC history loaded")

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    // Nếu biểu đồ lỗi, giá Ticker vẫn tiếp tục chạy.
                    Log.e("CRYPTO_CHART", "OHLC failed", e)
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