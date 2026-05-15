package com.example.tacbo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.tacbo.ui.theme.*
import androidx.navigation.compose.rememberNavController

@Composable
fun LandingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TacboGray1)
        ,
        contentAlignment = Alignment.Center
    ) {
        LandingBoardSelectionView(navController)
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(navController = rememberNavController())
}