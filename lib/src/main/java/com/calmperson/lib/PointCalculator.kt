package com.calmperson.lib

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

internal class PointCalculator(private val drawer: Drawer) {

    fun isPointInCenterButton(x: Float, y: Float): Boolean = with(drawer) {
        val distance = sqrt((centerX - x).toDouble().pow(2) + (centerY - y).toDouble().pow(2))
        return distance < innerRadius - centerButtonPadding - innerRadiusOffset
    }

    fun isPontInSector(x: Float, y: Float, startAngle: Int, endAngle: Int): Boolean {
        with(drawer) {
            val distance = sqrt((centerX - x).toDouble().pow(2) + (centerY - y).toDouble().pow(2))

            if (distance > outerRadius - outerRadiusOffset ||
                distance < innerRadius) {
                return false
            }

            val deltaX = x - centerX
            val deltaY = centerY - y

            val angleRadians = atan2(deltaY.toDouble(), deltaX.toDouble())
            var angle = Math.toDegrees(angleRadians)

            if (angle > 0) {
                angle -= 360.0
            }

            return abs(angle).toInt() in startAngle..endAngle
        }
    }

}