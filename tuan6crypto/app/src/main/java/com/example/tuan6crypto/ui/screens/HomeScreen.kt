package com.example.tuan6crypto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan6crypto.data.model.CoinType
import com.example.tuan6crypto.data.model.CoinUiModel
import com.example.tuan6crypto.ui.components.*
import com.example.tuan6crypto.ui.theme.CryptoBackground
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tuan6crypto.ui.viewmodel.HomeViewModel

import com.example.tuan6crypto.ui.theme.CryptoRed
import com.example.tuan6crypto.ui.theme.CryptoMuted

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.uiState.collectAsState()

    val coins = if (state.coins.isNotEmpty()) {
        state.coins
    } else {
        listOf(
            CoinUiModel(CoinType.ETH),
            CoinUiModel(CoinType.BTC),
            CoinUiModel(CoinType.LTC),
            CoinUiModel(CoinType.XMR)
        )
    }

    val connectionMessage = when {
        state.error != null -> "Connection lost"
        state.isLoading -> "Loading market data..."
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CryptoBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp)
    ) {

        // Home
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.07f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (connectionMessage != null) {
            Text(
                text = connectionMessage,
                color = if (state.error != null) CryptoRed else CryptoMuted,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // New
        SectionTitle("New")
        Spacer(modifier = Modifier.height(7.dp))

        BreakingNewsBanner(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.22f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Favorites: ETH và BTC
        SectionTitle("Favorites")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.27f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            coins.take(2).forEach { coin ->
                FavoriteCoinCard(
                    coin = coin,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // All Fluctuations: đủ bốn coin
        SectionTitle("All Fluctuations")
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.44f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            coins.forEach { coin ->
                FluctuationRow(
                    coin = coin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    )
}