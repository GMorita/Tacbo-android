package com.example.tacbo.ui.screens

import androidx.compose.ui.res.stringResource
import com.example.tacbo.R
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BoardScreen(navController: NavController) {
    val board = remember { Board() }

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
                FieldView(board = board)
            }
            
            BoardFooterView(
                modifier = Modifier.fillMaxWidth(),
                isDrawingMode = board.isDrawingMode,
                currentDrawingColor = board.drawingColor,
                onToggleP1 = { board.teamDataP1.isHidden = !board.teamDataP1.isHidden },
                onToggleP2 = { board.teamDataP2.isHidden = !board.teamDataP2.isHidden },
                onToggleDrawingMode = { board.isDrawingMode = !board.isDrawingMode },
                onSelectColor = { board.drawingColor = it },
                onClearDrawing = { board.drawingStrokes.clear() },
                onBack = { navController.navigate("landing") }
            )
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
fun BoardFooterView(
    modifier: Modifier = Modifier,
    isDrawingMode: Boolean = false,
    currentDrawingColor: Color = Color.Black,
    onToggleP1: () -> Unit = {},
    onToggleP2: () -> Unit = {},
    onToggleDrawingMode: () -> Unit = {},
    onSelectColor: (Color) -> Unit = {},
    onClearDrawing: () -> Unit = {},
    onBack: () -> Unit = {}
) {

    val systemBars = WindowInsets.systemBars.asPaddingValues()
    val statusBarHeight = systemBars.calculateBottomPadding()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.LightGray)
            .padding(bottom = statusBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isDrawingMode) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.DarkGray,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onToggleP1() }
            ) {
                Text(
                    text = "P1",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.DarkGray,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onToggleP2() }
            ) {
                Text(
                    text = "P2",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            // Color selection
            listOf(Color.Black, Color.Red, Color.Green, Color.Blue).forEach { color ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color,
                    border = if (currentDrawingColor == color) BorderStroke(2.dp, Color.White) else null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                        .clickable { onSelectColor(color) }
                ) {}
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.DarkGray,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onClearDrawing() }
            ) {
                Text(
                    text = "Clear",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isDrawingMode) Color.Blue else Color.DarkGray,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onToggleDrawingMode() }
        ) {
            Text(
                text = "Draw",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.DarkGray,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onBack() }
        ) {
            Text(
                text = stringResource(id = R.string.back_button),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.app_footer),
            fontSize = 20.sp,
            modifier = Modifier.height(60.dp).wrapContentSize(Alignment.Center)
        )
    }
}
