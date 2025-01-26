package com.example.parallel_connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.parallel_connect.screens.JeffChatScreen
import com.example.parallel_connect.screens.NavGraph
import com.example.parallel_connect.ui.theme.Parallel_ConnectTheme
import com.example.parallel_connect.viewModels.JeffChatViewModel
import com.example.parallel_connect.viewModels.MartinChatViewModel
import com.example.parallel_connect.viewModels.SarahChatViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        enableEdgeToEdge()
        setContent {
            Parallel_ConnectTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, viewModelJeff = JeffChatViewModel(), viewModelSarah = SarahChatViewModel(), viewModelMartin = MartinChatViewModel())
            }
        }
    }
}