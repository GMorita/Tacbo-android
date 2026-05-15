package com.example.tacbo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.tacbo.ui.common.FieldConstants
import com.example.tacbo.ui.screens.Field

data class FieldBounds(
    val fieldTopLeft: Offset,
    val fieldSize: Size,
    val scaleMeterToPix: Float
)

fun calculateFieldBounds(canvasSize: Size, field: Field): FieldBounds {
    val canvasWidth = canvasSize.width
    val canvasHeight = canvasSize.height

    val fieldHeight = field.vertical + FieldConstants.GOAL_DEPTH * 2
    val fieldAspectRatio = field.horizontal / fieldHeight
    val canvasAspectRatio = canvasWidth / canvasHeight

    val scaleMeterToPix = if (fieldAspectRatio > canvasAspectRatio) {
        canvasWidth / field.horizontal
    } else {
        canvasHeight / fieldHeight
    }

    val lineWidth = FieldConstants.LINE_WIDTH * scaleMeterToPix
    val goalLineWidth: Float = lineWidth * 0.5F

    val fieldTopLeftPixX = (canvasWidth - field.horizontal * scaleMeterToPix) / 2 + lineWidth
    val fieldTopLeftPixY = (canvasHeight - field.vertical * scaleMeterToPix) / 2 + goalLineWidth
    val fieldWidthPix = field.horizontal * scaleMeterToPix - lineWidth * 2f
    val fieldHeightPix = field.vertical * scaleMeterToPix - goalLineWidth * 2f

    return FieldBounds(
        fieldTopLeft = Offset(fieldTopLeftPixX, fieldTopLeftPixY),
        fieldSize = Size(fieldWidthPix, fieldHeightPix),
        scaleMeterToPix = scaleMeterToPix
    )
}

@Composable
fun FieldCanvas(modifier: Modifier = Modifier,
                field: Field = Field()
) {
    Canvas(modifier = modifier) {
        val lineColor = Color.Black

        val bounds = calculateFieldBounds(size, field)
        val fieldTopLeftPixX = bounds.fieldTopLeft.x
        val fieldTopLeftPixY = bounds.fieldTopLeft.y
        val fieldWidthPix = bounds.fieldSize.width
        val fieldHeightPix = bounds.fieldSize.height
        val scaleMeterToPix = bounds.scaleMeterToPix

        val lineWidth = FieldConstants.LINE_WIDTH * scaleMeterToPix

        drawRect(
            color = lineColor,
            topLeft = Offset(x = fieldTopLeftPixX, y = fieldTopLeftPixY),
            size = Size(width = fieldWidthPix, height = fieldHeightPix),
            style = Stroke(width = lineWidth)
        )

        val penaltyAreaTopLeftPixX = size.width * 0.5f - FieldConstants.PENALTY_AREA_WIDTH * scaleMeterToPix * 0.5f
        drawRect(
            color = lineColor,
            topLeft = Offset(x = penaltyAreaTopLeftPixX, y = fieldTopLeftPixY),
            size = Size(width = FieldConstants.PENALTY_AREA_WIDTH * scaleMeterToPix, height = FieldConstants.PENALTY_AREA_HEIGHT * scaleMeterToPix),
            style = Stroke(width = lineWidth)
        )

        val penaltyAreaTopLeftPixY = fieldTopLeftPixY + fieldHeightPix - FieldConstants.PENALTY_AREA_HEIGHT * scaleMeterToPix
        drawRect(
            color = lineColor,
            topLeft = Offset(x = penaltyAreaTopLeftPixX, y = penaltyAreaTopLeftPixY),
            size = Size(width = FieldConstants.PENALTY_AREA_WIDTH * scaleMeterToPix, height = FieldConstants.PENALTY_AREA_HEIGHT * scaleMeterToPix),
            style = Stroke(width = lineWidth)
        )

        val goalAreaTopLeftPixX = size.width * 0.5f - FieldConstants.GOAL_AREA_WIDTH * scaleMeterToPix * 0.5f
        drawRect(
            color = lineColor,
            topLeft = Offset(x = goalAreaTopLeftPixX, y = fieldTopLeftPixY),
            size = Size(width = FieldConstants.GOAL_AREA_WIDTH * scaleMeterToPix, height = FieldConstants.GOAL_AREA_HEIGHT * scaleMeterToPix),
            style = Stroke(width = lineWidth)
        )

        val goalAreaTopLeftPixY = fieldTopLeftPixY + fieldHeightPix - FieldConstants.GOAL_AREA_HEIGHT * scaleMeterToPix
        drawRect(
            color = lineColor,
            topLeft = Offset(x = goalAreaTopLeftPixX, y = goalAreaTopLeftPixY),
            size = Size(width = FieldConstants.GOAL_AREA_WIDTH * scaleMeterToPix, height = FieldConstants.GOAL_AREA_HEIGHT * scaleMeterToPix),
            style = Stroke(width = lineWidth)
        )

        val goalTopLeftPixX = size.width * 0.5f - FieldConstants.GOAL_WIDTH * scaleMeterToPix * 0.5f
        val goalTopLeftPixY = fieldTopLeftPixY - FieldConstants.GOAL_DEPTH * scaleMeterToPix

        drawRect(
            color = lineColor,
            topLeft = Offset(x = goalTopLeftPixX, y = goalTopLeftPixY),
            size = Size(width = FieldConstants.GOAL_WIDTH * scaleMeterToPix, height = FieldConstants.GOAL_DEPTH * scaleMeterToPix),
            style = Stroke(width = lineWidth * 0.5f)
        )

        drawRect(
            color = lineColor,
            topLeft = Offset(x = goalTopLeftPixX, y = goalTopLeftPixY + fieldHeightPix + FieldConstants.GOAL_DEPTH * scaleMeterToPix),
            size = Size(width = FieldConstants.GOAL_WIDTH * scaleMeterToPix, height = FieldConstants.GOAL_DEPTH * scaleMeterToPix),
            style = Stroke(width = lineWidth * 0.5f)
        )

        drawLine(
            color = lineColor,
            start = Offset(x = fieldTopLeftPixX, y = size.height * 0.5f),
            end = Offset(x = fieldTopLeftPixX + fieldWidthPix, y = size.height * 0.5f),
            strokeWidth = lineWidth
        )

        drawCircle(
            color = lineColor,
            radius = FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix,
            center = Offset(x = size.width * 0.5f, y = size.height * 0.5f),
            style = Stroke(width = lineWidth)
        )

        // Draw Lanes (Vertical lines)
        if (!field.divisionLineIsHidden && field.laneNum > 1) {
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(1f * scaleMeterToPix, 1f * scaleMeterToPix), 0f)
            for (i in 1 until field.laneNum) {
                val x = fieldTopLeftPixX + (fieldWidthPix / field.laneNum) * i
                drawLine(
                    color = lineColor,
                    start = Offset(x = x, y = fieldTopLeftPixY),
                    end = Offset(x = x, y = fieldTopLeftPixY + fieldHeightPix),
                    strokeWidth = lineWidth * 0.5f,
                    pathEffect = dashEffect
                )
            }
        }

        // Draw Zones (Horizontal lines)
        if (!field.divisionLineIsHidden && field.zoneNum > 1) {
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(1f * scaleMeterToPix, 1f * scaleMeterToPix), 0f)
            for (i in 1 until field.zoneNum) {
                val y = fieldTopLeftPixY + (fieldHeightPix / field.zoneNum) * i
                drawLine(
                    color = lineColor,
                    start = Offset(x = fieldTopLeftPixX, y = y),
                    end = Offset(x = fieldTopLeftPixX + fieldWidthPix, y = y),
                    strokeWidth = lineWidth * 0.5f,
                    pathEffect = dashEffect
                )
            }
        }

        drawCircle(
            color = lineColor,
            radius = FieldConstants.PENALTY_MARK_RADIUS * scaleMeterToPix,
            center = Offset(x = size.width * 0.5f, y = fieldTopLeftPixY + FieldConstants.PENALTY_MARK_DIS * scaleMeterToPix)
        )

        drawCircle(
            color = lineColor,
            radius = FieldConstants.PENALTY_MARK_RADIUS * scaleMeterToPix,
            center = Offset(x = size.width * 0.5f, y = fieldTopLeftPixY + fieldHeightPix - FieldConstants.PENALTY_MARK_DIS * scaleMeterToPix)
        )

        drawArc(
            color = lineColor,
            topLeft = Offset(x = size.width * 0.5f - FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY + FieldConstants.PENALTY_MARK_DIS * scaleMeterToPix - FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix),
            size = Size(FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix * 2f, FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix * 2f),
            startAngle = 36.5f,
            sweepAngle = 107f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )

        drawArc(
            color = lineColor,
            topLeft = Offset(x = size.width * 0.5f - FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY + fieldHeightPix - FieldConstants.PENALTY_MARK_DIS * scaleMeterToPix - FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix),
            size = Size(FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix * 2f, FieldConstants.CENTER_CIRCLE_RADIUS * scaleMeterToPix * 2f),
            startAngle = 216.5f,
            sweepAngle = 107f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )


        val cornerArcDiameterPix = FieldConstants.CORNER_ARC_RADIUS * 2f * scaleMeterToPix
        drawArc(
            color = lineColor,
            topLeft = Offset(x = fieldTopLeftPixX - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix),
            size = Size(cornerArcDiameterPix, cornerArcDiameterPix),
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )

        drawArc(
            color = lineColor,
            topLeft = Offset(x = fieldTopLeftPixX + fieldWidthPix - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix),
            size = Size(cornerArcDiameterPix, cornerArcDiameterPix),
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )

        drawArc(
            color = lineColor,
            topLeft = Offset(x = fieldTopLeftPixX - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY + fieldHeightPix - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix),
            size = Size(cornerArcDiameterPix, cornerArcDiameterPix),
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )

        drawArc(
            color = lineColor,
            topLeft = Offset(x = fieldTopLeftPixX + fieldWidthPix - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix, y = fieldTopLeftPixY + fieldHeightPix - FieldConstants.CORNER_ARC_RADIUS * scaleMeterToPix),
            size = Size(cornerArcDiameterPix, cornerArcDiameterPix),
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = lineWidth)
        )
    }
}