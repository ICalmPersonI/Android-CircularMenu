package com.calmperson.lib

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.calmperson.lib.Utils.pixelsToDp
import com.calmperson.lib.circularmenu.R
import com.calmperson.lib.model.AnimationProperties
import com.calmperson.lib.model.GeometricProperties

internal class Drawer(private val circularMenu: CircularMenu) {

    companion object {
        internal val DEFAULT_COLOR = Color.argb(128, 128, 128, 128)
        internal val DEFAULT_PRESSED_BUTTON_COLOR = Color.argb(128, 0, 255, 255)
        internal val DEFAULT_COLOR_RES = R.color.color_default
        internal val DEFAULT_COLORS_RES = R.array.colors
        internal val DEFAULT_PRESSED_BUTTON_COLOR_RES = R.color.pressed_color
        internal val DEFAULT_CENTER_BUTTON_ICON_RES_ID = R.drawable.cancel
        internal const val DEFAULT_BUTTON_SPACING = 10f
        internal const val DEFAULT_CENTER_BUTTON_PADDING = 10f
    }

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    val animationProperties = AnimationProperties()
    var geometricProperties = GeometricProperties(
        outerRectFLeft = 0f,
        outerRectFTop = 0f,
        outerRectFRight = 0f,
        outerRectFBottom = 0f,
        centerX = 0f,
        centerY = 0f,
        outerRadius = 0f,
        innerRadius = 0f,
        sectorSpacing = DEFAULT_BUTTON_SPACING.pixelsToDp(circularMenu.context),
        centerButtonPadding = DEFAULT_CENTER_BUTTON_PADDING.pixelsToDp(circularMenu.context)
    )

    private val geometryCalculator = GeometryCalculator()

    var centerButtonColor: Int = DEFAULT_COLOR
    var centerButtonCurrentColor: Int = DEFAULT_COLOR
    var centerButtonPressedColor: Int = DEFAULT_PRESSED_BUTTON_COLOR
    var centerButtonIconResId: Int = DEFAULT_CENTER_BUTTON_ICON_RES_ID

    var sectorPressedColor: Int = DEFAULT_PRESSED_BUTTON_COLOR

    fun Canvas.drawSectors() {
        for (i in circularMenu.sectors.indices) {
            val sector = circularMenu.sectors[i]
            paint.color = sector.currentColor
            geometryCalculator.calcSectorParameters(
                sectorIndex = i,
                animatedSectorIndex = animationProperties.animatedSectorIndex,
                startAngle = sector.startAngle.toFloat(),
                endAngle = sector.endAngle.toFloat(),
                angleOffset = animationProperties.angleOffset,
                outerRadius = geometricProperties.outerRadius,
                outerRadiusOffset = animationProperties.outerRadiusOffset,
                centerY = geometricProperties.centerY,
                centerX = geometricProperties.centerX,
                cascadeAnimationMode = animationProperties.cascadeAnimationMode,
                outerRectFLeft = geometricProperties.outerRectFLeft,
                outerRectFTop = geometricProperties.outerRectFTop,
                outerRectFRight = geometricProperties.outerRectFRight,
                outerRectFBottom = geometricProperties.outerRectFBottom,
            )?.let { param ->
                sector.rectF.left = param.left
                sector.rectF.top = param.top
                sector.rectF.right = param.right
                sector.rectF.bottom = param.bottom
                drawArc(sector.rectF, param.startAngle, param.sweepAngle, true, paint)
            }
        }
    }

    fun Canvas.drawIcons() {
        for (i in circularMenu.sectors.indices) {
            val sector = circularMenu.sectors[i]
            if (animationProperties.animatedSectorIndex > i &&
                animationProperties.cascadeAnimationMode) continue
            ResourcesCompat.getDrawable(
                circularMenu.context.resources,
                sector.iconId,
                circularMenu.context.theme
            )?.let { drawable ->
                val (left, top, right, bottom) = geometryCalculator.calcSectorIconBounds(
                    sectorIndex = i,
                    animatedSectorIndex = animationProperties.animatedSectorIndex,
                    startAngle = sector.startAngle.toFloat(),
                    endAngle = sector.endAngle.toFloat(),
                    outerRadius = geometricProperties.outerRadius,
                    outerRadiusOffset = animationProperties.outerRadiusOffset,
                    innerRadius = geometricProperties.innerRadius,
                    centerX = geometricProperties.centerX,
                    centerY = geometricProperties.centerY
                )
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(this@drawIcons)
            }
        }
    }

    fun Canvas.clearCenter()  {
        paint.xfermode = xfermode
        val radius = geometricProperties.innerRadius
        drawCircle(geometricProperties.centerX, geometricProperties.centerY, radius, paint)
        paint.xfermode = null
    }

    fun Canvas.drawCenterButton() = with(geometricProperties) {
        val radius = geometryCalculator.calcCenterButtonRadius(
            innerRadius = geometricProperties.innerRadius,
            innerRadiusOffset = animationProperties.innerRadiusOffset,
            centerButtonPadding = geometricProperties.centerButtonPadding
        )
        circularMenu.rotation = animationProperties.centerButtonRotation
        paint.color = centerButtonCurrentColor
        drawCircle(centerX, centerY, radius, paint)
        AppCompatResources.getDrawable(circularMenu.context, R.drawable.cancel)?.let { drawable ->
            val (left, top, right, bottom) = geometryCalculator.calcCenterButtonIconBounds(
                radius = radius,
                centerX = geometricProperties.centerX,
                centerY = geometricProperties.centerY
            )
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(this@drawCenterButton)
        }
    }

    fun Canvas.drawSectorSpacers() {
        paint.xfermode = xfermode
        for (i in circularMenu.sectors.indices) {
            val sector = circularMenu.sectors[i]
            if (animationProperties.animatedSectorIndex > i &&
                animationProperties.cascadeAnimationMode) {
                continue
            }
            for (angle in listOf(sector.startAngle, sector.endAngle)) {
                val (startX, startY, endX, endY, width) = geometryCalculator.calcSectorSpacerPoints(
                    sectorIndex = i,
                    animatedSectorIndex = animationProperties.animatedSectorIndex,
                    angle = angle.toDouble(),
                    outerRadius = geometricProperties.outerRadius,
                    outerRadiusOffset = animationProperties.outerRadiusOffset,
                    innerRadius = geometricProperties.innerRadius,
                    innerRadiusOffset = animationProperties.innerRadiusOffset,
                    centerX = geometricProperties.centerX,
                    centerY = geometricProperties.centerY,
                    centerButtonPadding = geometricProperties.centerButtonPadding,
                    sectorSpacing = geometricProperties.sectorSpacing
                )
                paint.strokeWidth = width
                drawLine(startX, startY, endX, endY, paint)
            }
        }
        paint.xfermode = null
    }

}