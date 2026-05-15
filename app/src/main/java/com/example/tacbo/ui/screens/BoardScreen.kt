package com.example.tacbo.ui.screens

import androidx.compose.ui.res.stringResource
import com.example.tacbo.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import com.example.tacbo.calculateFieldBounds
import com.example.tacbo.FieldBounds
import androidx.compose.ui.geometry.Size
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tacbo.FieldCanvas
import kotlin.math.roundToInt

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



enum class FieldType {
    ELEVEN
}

class Field {
    var vertical: Float = 105f
    var horizontal: Float = 68f

    var divisionLineIsHidden: Boolean = false
    var laneNum: Int = 2
    var zoneNum: Int = 3

    val defaultPlayerSizeMeter: Float = 1.8f

    var isDisplayAsVertical: Boolean = true
    var enlargeRatio: Float = 1f

    var displayFiveLane: Boolean = false
    var displaySevenLane: Boolean = false
    var displayBielsaLine: Boolean = false

    var fieldType: FieldType = FieldType.ELEVEN
}

class PlayerPosition(var x: Float = 0f, var y: Float = 0f)

class Player {
    var position: PlayerPosition = PlayerPosition()
    var dynamicPositions: MutableList<PlayerPosition> = mutableListOf()
    var name: String = ""
}

class Team {
    var color: Color = Color.Red
    var playerSize: Float = 3f
    var playerZone: Float = 3f

    var player1 = Player()
    var player2 = Player()
    var player3 = Player()
    var player4 = Player()
    var player5 = Player()
    var player6 = Player()
    var player7 = Player()
    var player8 = Player()
    var player9 = Player()
    var player10 = Player()
    var player11 = Player()

    var isHidden = false
    var labelIsHidden = true

    fun getPlayerList(): List<Player> {
        return listOf(
            player1,
            player2,
            player3,
            player4,
            player5,
            player6,
            player7,
            player8,
            player9,
            player10,
            player11)
    }
}

class Board {
    var delay: Double = 0.0
    var fieldData: Field = Field()
    var teamDataP1: Team = Team().apply {
        labelIsHidden = false
        getPlayerList().forEachIndexed { index, it ->
            it.position.x = (0..100).random().toFloat() / 100f
            it.position.y = (0..100).random().toFloat() / 100f
            it.name = "P1-${index + 1}"
        }
    }
    var teamDataP2: Team = Team().apply {
        color = Color.Blue
        labelIsHidden = false
        getPlayerList().forEachIndexed { index, it ->
            it.position.x = (0..100).random().toFloat() / 100f
            it.position.y = (0..100).random().toFloat() / 100f
            it.name = "P2-${index + 1}"
        }
    }
}

@Composable
fun FieldView(board: Board = Board()) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        val canvasSize = Size(constraints.maxWidth.toFloat(), this.constraints.maxHeight.toFloat())
        val bounds = calculateFieldBounds(canvasSize, board.fieldData)

        FieldCanvas(
            field = board.fieldData,
            modifier = Modifier.fillMaxSize()
        )

        TeamView(teamData = board.teamDataP1, fieldBounds = bounds, modifier = Modifier.fillMaxSize())
        TeamView(teamData = board.teamDataP2, fieldBounds = bounds, modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun TeamView(modifier: Modifier = Modifier, teamData: Team = Team(), fieldBounds: FieldBounds) {
    if (teamData.isHidden) return

    Box(
        modifier = modifier
    ) {
        val playerList = teamData.getPlayerList()
        playerList.forEach { player ->

            PlayerView(
                player = player,
                fieldBounds = fieldBounds,
                color = teamData.color,
                labelIsHidden = teamData.labelIsHidden
            )


        }
    }
}

@Composable
fun PlayerView(
    player: Player,
    fieldBounds: FieldBounds,
    color: Color = Color.Blue,
    labelIsHidden: Boolean = false
) {

    var positionX by remember { mutableFloatStateOf(player.position.x) }
    var positionY by remember { mutableFloatStateOf(player.position.y) }

    val density = LocalDensity.current
    val boxSize = 40.dp
    val boxSizePx = with(density) { boxSize.toPx() }

    val centerX = fieldBounds.fieldTopLeft.x + positionX * fieldBounds.fieldSize.width
    val centerY = fieldBounds.fieldTopLeft.y + positionY * fieldBounds.fieldSize.height

    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    (centerX - boxSizePx / 2).roundToInt(),
                    (centerY - boxSizePx / 2).roundToInt()
                )
            }
            .pointerInput(fieldBounds) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    positionX += dragAmount.x / fieldBounds.fieldSize.width
                    positionY += dragAmount.y / fieldBounds.fieldSize.height
                    player.position.x = positionX
                    player.position.y = positionY
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(color.copy(alpha = 0.5f), shape = RoundedCornerShape(20.dp))
        )
        if (!labelIsHidden) {
            Text(
                text = player.name,
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun BoardScreenScreenPreview() {
    FieldView()
}