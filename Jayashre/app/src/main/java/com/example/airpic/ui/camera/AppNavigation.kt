package com.example.airpic.ui.camera

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFragment"
    ) {
        composable("mainFragment") {
            CameraScreen(navController)
        }
        composable("infoFragment") {
            InfoScreen(navController)
        }
    }
}
