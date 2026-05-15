package com.example.tacbo.ui.screens

import androidx.compose.ui.res.stringResource
import com.example.tacbo.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BoardScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {

            BoardHeaderView(
                navController,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Box(modifier = Modifier
                .weight(1f)
            ) {
                FieldView()
            }
            
            BoardFooterView(modifier = Modifier.fillMaxWidth())
        }

        Column {

            BoardHeaderView(
                navController,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            
            BoardFooterView(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun BoardHeaderView( navController: NavController, modifier: Modifier = Modifier) {

    val systemBars = WindowInsets.systemBars.asPaddingValues()
    val statusBarHeight = systemBars.calculateTopPadding()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(top = statusBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray,
            modifier = Modifier
                .clickable {
                    navController.navigate("landing")
                },
        ) {
            Text(
                text = stringResource(id = R.string.back_button),
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier
                    .height(60.dp)
                    .width(140.dp)
                    .wrapContentSize(Alignment.Center)

            )
        }
        Text(text = stringResource(id = R.string.app_header), fontSize = 20.sp)
    }
}

@Composable
fun BoardFooterView(modifier: Modifier = Modifier) {

    val systemBars = WindowInsets.systemBars.asPaddingValues()
    val statusBarHeight = systemBars.calculateBottomPadding()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(bottom = statusBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.app_footer),
            fontSize = 20.sp,
            modifier = Modifier.height(60.dp)
        )
    }
}
