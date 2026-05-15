package com.example.tacbo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tacbo.FieldCanvas
import com.example.tacbo.R

@Composable
fun LandingBoardSelectionView(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val imagePainter = painterResource(id = R.drawable.img_board_frame)
            Image(
                painter = imagePainter,
                contentDescription = "Board Frame",
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
            )

            FieldCanvas(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .padding(horizontal = 30.dp, vertical = 40.dp)
            )
        }

        Column {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray,
                modifier = Modifier.clickable {
                    println("BOARD tapped")
                    navController.navigate("board")
                }
            ) {
                Text(
                    text = "BOARD",
                    color = Color.Black,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(140.dp)
                        .wrapContentSize(Alignment.Center)

                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.DarkGray,
                modifier = Modifier.clickable {
                    println("BUILD tapped")
                }
            ) {
                Text(
                    text = "BUILD",
                    color = Color.White,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(140.dp)
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}