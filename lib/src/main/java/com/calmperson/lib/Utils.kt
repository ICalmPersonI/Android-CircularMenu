package com.calmperson.lib

import android.content.Context
import android.util.DisplayMetrics

object Utils {

    fun Float.pixelsToDp(context: Context): Float {
        return this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}