package com.example.tuan6crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tuan6crypto.ui.screens.HomeScreen
import com.example.tuan6crypto.ui.theme.Tuan6cryptoTheme


import androidx.lifecycle.ViewModelProvider
import com.example.tuan6crypto.ui.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        enableEdgeToEdge()

        setContent {
            Tuan6cryptoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1C1D29)
                ) {
                    HomeScreen(viewModel = homeViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.startPolling()
    }

    override fun onStop() {
        homeViewModel.stopPolling()
        super.onStop()
    }
}