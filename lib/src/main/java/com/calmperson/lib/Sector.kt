package com.calmperson.lib

import android.graphics.RectF
import androidx.annotation.DrawableRes

data class Sector(
    internal val rectF: RectF,
    internal val startAngle: Int,
    internal val endAngle: Int,
    @DrawableRes var icon: Int,
    var defaultColor: Int,
    var currentColor: Int,
    var startCloseAnimationAfterClick: Boolean,
)