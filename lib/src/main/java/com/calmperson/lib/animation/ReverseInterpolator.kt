package com.calmperson.lib.animation

import android.view.animation.Interpolator

internal class ReverseInterpolator : Interpolator {
    override fun getInterpolation(paramFloat: Float): Float {
        return 1f - paramFloat
    }

}