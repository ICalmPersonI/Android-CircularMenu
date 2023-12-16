package com.calmperson.lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import kotlin.math.cos
import kotlin.math.sin

internal class Drawer(private val circularMenu: CircularMenu) {

    companion object {
        internal val DEFAULT_COLOR = Color.argb(128, 128, 128, 128)
        internal val PRESSED_BUTTON_COLOR = Color.argb(128, 0, 255, 255)
        internal val DEFAULT_CENTER_BUTTON_ICON_RES_ID = R.drawable.cancel
        internal const val DEFAULT_BUTTON_SPACING = 10f
        internal const val DEFAULT_CENTER_BUTTON_PADDING = 10f
    }

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    var outerRectFLeft = 0f
    var outerRectFTop = 0f
    var outerRectFRight = 0f
    var outerRectFBottom = 0f

    var centerX = 0f
    var centerY = 0f

    var outerRadius = 0f
    var innerRadius = 0f

    var buttonSpacing = DEFAULT_BUTTON_SPACING
        set(value) { field = value.pixelsToDp(circularMenu.context) }

    var centerButtonColor: Int = DEFAULT_COLOR
    var centerButtonColorConst = DEFAULT_COLOR
    var centerButtonIconResId: Int = DEFAULT_CENTER_BUTTON_ICON_RES_ID
    var centerButtonPadding = DEFAULT_CENTER_BUTTON_PADDING
        set(value) { field = value.pixelsToDp(circularMenu.context)}

    var animatedSectorIndex = 0
    var outerRadiusOffset = 0f
    var innerRadiusOffset = 0f
    var angleOffset = 0f

    fun Canvas.drawSectors() = with(circularMenu) {
        for (i in sectors.indices) {
            val rect = sectors[i].rectF
            val startAngle = sectors[i].startAngle.toFloat()
            val endAngle = sectors[i].endAngle.toFloat()
            paint.color = sectors[i].currentColor
            if (animatedSectorIndex == i) {
                val sweepAngle = (endAngle - angleOffset) - (startAngle + angleOffset)
                val radius = outerRadius - outerRadiusOffset
                rect.left = centerX - radius
                rect.top = centerY - radius
                rect.right = centerX + radius
                rect.bottom = centerY + radius
                drawArc(rect, startAngle + angleOffset, sweepAngle, true, paint)
            } else if (i > animatedSectorIndex) {
                val sweepAngel = endAngle - startAngle
                rect.left = outerRectFLeft
                rect.top = outerRectFTop
                rect.right = outerRectFRight
                rect.bottom = outerRectFBottom
                drawArc(rect, startAngle, sweepAngel, true, paint)
            }
            /*
            else {
                val sweepAngel = endAngle - startAngle
                getBackgroundColor()?.let { color ->
                    paint.color = color
                } ?: run {
                    paint.xfermode = xfermode
                }
                drawArc(rect, startAngle, sweepAngel, true, paint)
                paint.xfermode = null
            }

             */
        }
    }

    fun Canvas.drawIcons() = with(circularMenu) {
        for (i in sectors.indices) {
            if (animatedSectorIndex > i) continue
            AppCompatResources.getDrawable(context, sectors[i].icon)?.let { drawable ->
                val outerRadius = outerRadius - (if (animatedSectorIndex == i) outerRadiusOffset else 0f)
                val innerRadius = innerRadius
                val centerAngle = (sectors[i].startAngle + sectors[i].endAngle) / 2
                val angleInRadians = Math.toRadians(centerAngle.toDouble())

                val startX = (centerX + (innerRadius * cos(angleInRadians))).toFloat()
                val startY = (centerY + (innerRadius * sin(angleInRadians))).toFloat()
                val endX = (centerX + (outerRadius * cos(angleInRadians))).toFloat()
                val endY = (centerY + (outerRadius * sin(angleInRadians))).toFloat()

                val x = ((startX + endX) / 2).toInt()
                val y = ((startY + endY) / 2).toInt()
                val padding = (((outerRadius - innerRadius) / 2) / 1.5).toInt()

                drawable.setBounds(
                    x - padding,
                    y - padding,
                    x + padding,
                    y + padding
                )

                drawable.draw(this@drawIcons)
            }
        }
    }

    fun Canvas.clearCenter() = with(circularMenu) {
        getBackgroundColor()?.let { color ->
            paint.color = color
        } ?: run {
            paint.xfermode = xfermode
        }
        drawCircle(centerX, centerY, innerRadius - innerRadiusOffset, paint)
        paint.xfermode = null
    }

    fun Canvas.drawCenterButton() = with(circularMenu) {
        val circleRadius = innerRadius - centerButtonPadding.pixelsToDp(context) - innerRadiusOffset
        paint.color = centerButtonColor
        drawCircle(centerX, centerY, circleRadius, paint)
        AppCompatResources.getDrawable(context, R.drawable.cancel)?.let { drawable ->
            val imagePadding = circleRadius - (circleRadius / 4)
            drawable.setBounds(
                (centerX - imagePadding).toInt(),
                (centerY - imagePadding).toInt(),
                (centerX + imagePadding).toInt(),
                (centerY + imagePadding).toInt()
            )
            drawable.draw(this@drawCenterButton)
        }
    }

    fun Canvas.drawSectorSpacers() = with(circularMenu) {
        getBackgroundColor()?.let { color ->
            paint.color = color
        } ?: run {
            paint.xfermode = xfermode
        }
        paint.strokeWidth = buttonSpacing.pixelsToDp(context)
        for (i in sectors.indices) {
            if (animatedSectorIndex > i) continue
            val outerRadius = outerRadius - (if (animatedSectorIndex == i) outerRadiusOffset else 0f)
            for (angle in listOf(sectors[i].startAngle, sectors[i].endAngle)) {
                val angleInRadians = Math.toRadians(angle.toDouble())
                val startX = (centerX + (innerRadius * cos(angleInRadians))).toFloat()
                val startY = (centerY + (innerRadius * sin(angleInRadians))).toFloat()
                val endX = (centerX + (outerRadius * cos(angleInRadians))).toFloat()
                val endY = (centerY + (outerRadius * sin(angleInRadians))).toFloat()
                drawLine(startX, startY, endX, endY, paint)
            }
        }
        paint.xfermode = null
    }

    private fun CircularMenu.getBackgroundColor(): Int? {
        return if (background is ColorDrawable) (background as ColorDrawable).color else null
    }

    private fun Float.pixelsToDp(context: Context): Float {
        return this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}