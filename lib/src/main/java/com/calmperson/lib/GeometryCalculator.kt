package com.calmperson.lib


import com.calmperson.lib.model.IconBounds
import com.calmperson.lib.model.SectorProperties
import com.calmperson.lib.model.SectorSpacerProperties
import kotlin.math.cos
import kotlin.math.sin

internal class GeometryCalculator {

    fun calcSectorParameters(
        sectorIndex: Int,
        animatedSectorIndex: Int,
        startAngle: Float,
        endAngle: Float,
        angleOffset: Float,
        outerRadius: Float,
        outerRadiusOffset: Float,
        centerY: Float,
        centerX: Float,
        cascadeAnimationMode: Boolean,
        outerRectFLeft: Float,
        outerRectFTop: Float,
        outerRectFRight: Float,
        outerRectFBottom: Float,
    ): SectorProperties? {
        return when {
            animatedSectorIndex == sectorIndex -> {
                val radius = outerRadius - outerRadiusOffset
                SectorProperties(
                    startAngle = startAngle + angleOffset,
                    sweepAngle = (endAngle - angleOffset) - (startAngle + angleOffset),
                    left = centerX - radius,
                    top = centerY - radius,
                    right = centerX + radius,
                    bottom = centerY + radius
                )
            }
            sectorIndex > animatedSectorIndex && cascadeAnimationMode -> {
                SectorProperties(
                    startAngle = startAngle,
                    sweepAngle = endAngle - startAngle,
                    left = outerRectFLeft,
                    top = outerRectFTop,
                    right = outerRectFRight,
                    bottom = outerRectFBottom
                )
            }
            !cascadeAnimationMode -> {
                SectorProperties(
                    startAngle = startAngle,
                    sweepAngle = endAngle - startAngle,
                    left = outerRectFLeft,
                    top = outerRectFTop,
                    right = outerRectFRight,
                    bottom = outerRectFBottom
                )
            }
            else -> null
        }
    }

    fun calcSectorIconBounds(
        sectorIndex: Int,
        animatedSectorIndex: Int,
        startAngle: Float,
        endAngle: Float,
        outerRadius: Float,
        outerRadiusOffset: Float,
        innerRadius: Float,
        centerX: Float,
        centerY: Float
    ): IconBounds {
        val newOuterRadius = if (animatedSectorIndex == sectorIndex) {
            outerRadius - outerRadiusOffset
        } else {
            outerRadius
        }

        val centerAngle = ((startAngle + endAngle) / 2).toDouble()
        val angleInRadians = Math.toRadians(centerAngle)
        val radius = newOuterRadius - (newOuterRadius - innerRadius) / 2
        val x = centerX + radius * cos(angleInRadians)
        val y = centerY + radius * sin(angleInRadians)

        val padding = (newOuterRadius - innerRadius) / 2 / 1.5

        return IconBounds(
            left = (x - padding).toInt(),
            top = (y - padding).toInt(),
            right = (x + padding).toInt(),
            bottom = (y + padding).toInt()
        )
    }

    fun calcCenterButtonRadius(
        innerRadius: Float,
        innerRadiusOffset: Float,
        centerButtonPadding: Float
    ): Float {
        return innerRadius - centerButtonPadding - innerRadiusOffset
    }

    fun calcCenterButtonIconBounds(radius: Float, centerX: Float, centerY: Float): IconBounds {
        val imagePadding = radius - (radius / 4)
        return IconBounds(
            left = (centerX - imagePadding).toInt(),
            top = (centerY - imagePadding).toInt(),
            right = (centerX + imagePadding).toInt(),
            bottom = (centerY + imagePadding).toInt()
        )
    }

    fun calcSectorSpacerPoints(
        sectorIndex: Int,
        animatedSectorIndex: Int,
        angle: Double,
        outerRadius: Float,
        outerRadiusOffset: Float,
        innerRadius: Float,
        innerRadiusOffset: Float,
        centerX: Float,
        centerY: Float,
        centerButtonPadding: Float,
        sectorSpacing: Float
    ): SectorSpacerProperties {
        val newOuterRadius = if (animatedSectorIndex == sectorIndex) {
            outerRadius - outerRadiusOffset
        } else {
            outerRadius
        }
        val angleInRadians = Math.toRadians(angle)
        val newInnerRadius = innerRadius - centerButtonPadding - innerRadiusOffset
        return SectorSpacerProperties(
            startX = (centerX + newInnerRadius * cos(angleInRadians)).toFloat(),
            startY = (centerY + newInnerRadius * sin(angleInRadians)).toFloat(),
            endX = (centerX + newOuterRadius * cos(angleInRadians)).toFloat(),
            endY = (centerY + newOuterRadius * sin(angleInRadians)).toFloat(),
            width = sectorSpacing / 2
        )
    }
}

