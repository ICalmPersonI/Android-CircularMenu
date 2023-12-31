package com.calmperson.lib

import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal class PointCalculator {

    fun isPointInCenterButton(
        x: Float,
        y: Float,
        centerX: Float,
        centerY: Float,
        innerRadius: Float,
        centerButtonPadding: Float
    ): Boolean {
        val distance = sqrt(
            (centerX - x).toDouble().pow(2) + (centerY - y).toDouble().pow(2)
        ).toBigDecimal().setScale(4, RoundingMode.HALF_UP).toFloat()
        return distance <= innerRadius - centerButtonPadding
    }

    fun isPontInSector(
        x: Float,
        y: Float,
        startAngle: Int,
        endAngle: Int,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
    ): Boolean {
        val distance = sqrt(
            (centerX - x).toDouble().pow(2) + (centerY - y).toDouble().pow(2)
        ).toBigDecimal().setScale(4, RoundingMode.HALF_UP).toFloat()

        if (distance < innerRadius || distance > outerRadius) return false

        val deltaX = x - centerX
        val deltaY = centerY - y

        val angleRadians = atan2(deltaY.toDouble(), deltaX.toDouble())
        var angle = Math.toDegrees(angleRadians)

        if (angle > 0) angle -= 360.0

        return abs(angle).roundToInt() in startAngle..endAngle
    }

}