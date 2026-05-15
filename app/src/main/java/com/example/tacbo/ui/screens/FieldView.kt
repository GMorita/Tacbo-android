package com.example.tacbo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tacbo.FieldBounds
import com.example.tacbo.FieldCanvas
import com.example.tacbo.calculateFieldBounds
import kotlin.math.roundToInt

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
    var playerSize: Float = 1.8f
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

        TeamView(teamData = board.teamDataP1, fieldBounds = bounds, modifier = Modifier.fillMaxSize(), isFacingUp = true)
        TeamView(teamData = board.teamDataP2, fieldBounds = bounds, modifier = Modifier.fillMaxSize(), isFacingUp = false)
    }
}

@Composable
fun TeamView(modifier: Modifier = Modifier, teamData: Team = Team(), fieldBounds: FieldBounds, isFacingUp: Boolean = true) {
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
                labelIsHidden = teamData.labelIsHidden,
                playerSize = teamData.playerSize,
                playerZone = teamData.playerZone,
                isFacingUp = isFacingUp
            )


        }
    }
}

@Composable
fun PlayerView(
    player: Player,
    fieldBounds: FieldBounds,
    color: Color = Color.Blue,
    labelIsHidden: Boolean = false,
    playerSize: Float = 1.8f,
    playerZone: Float = 3f,
    isFacingUp: Boolean = true
) {

    var positionX by remember { mutableFloatStateOf(player.position.x) }
    var positionY by remember { mutableFloatStateOf(player.position.y) }

    val playerZonePx = playerZone * fieldBounds.scaleMeterToPix
    val playerSizePx = playerSize * fieldBounds.scaleMeterToPix

    val centerX = fieldBounds.fieldTopLeft.x + positionX * fieldBounds.fieldSize.width
    val centerY = fieldBounds.fieldTopLeft.y + positionY * fieldBounds.fieldSize.height

    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    (centerX - playerZonePx / 2).roundToInt(),
                    (centerY - playerZonePx / 2).roundToInt()
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
        val density = LocalDensity.current
        Canvas(
            modifier = Modifier
                .size(with(density) { playerZonePx.toDp() })
        ) {
            drawCircle(
                color = color.copy(alpha = 0.5f),
                radius = size.minDimension / 2
            )
            drawCircle(
                color = color,
                radius = size.minDimension / 2,
                style = Stroke(width = 2.dp.toPx())
            )

            drawCircle(
                color = color,
                radius = playerSizePx / 2
            )

            val r = playerSizePx / 2f
            val cx = size.width / 2f
            val cy = size.height / 2f

            val trianglePath = Path().apply {
                if (isFacingUp) {
                    moveTo(cx, cy - r * 0.4f)
                    lineTo(cx - r * 1f, cy + r * 0.5f)
                    lineTo(cx + r * 1f, cy + r * 0.5f)
                } else {
                    moveTo(cx, cy + r * 0.4f)
                    lineTo(cx - r * 1f, cy - r * 0.5f)
                    lineTo(cx + r * 1f, cy - r * 0.5f)
                }
                close()
            }
            drawPath(path = trianglePath, color = Color.White)
        }
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
