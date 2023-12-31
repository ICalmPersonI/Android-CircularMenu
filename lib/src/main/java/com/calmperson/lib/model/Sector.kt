package com.calmperson.lib.model

import android.graphics.RectF
import androidx.annotation.DrawableRes

/**
 * @param color default sector color, not to be confused with `currentColor`.
 */
data class Sector(
    internal val rectF: RectF,
    internal val startAngle: Int,
    internal val endAngle: Int,
    var currentColor: Int,
    @DrawableRes var iconId: Int,
    var color: Int,
    var pressedColor: Int,
)