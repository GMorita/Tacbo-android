package com.example.tacbo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tacbo.ui.screens.BoardScreen
import com.example.tacbo.ui.screens.LandingScreen

@Composable
fun TacboNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen(navController)
        }
        composable("board") {
            BoardScreen(navController)
        }
    }
}