package com.calmperson.lib

import android.content.Context
import android.content.res.TypedArray
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.ArrayRes

object TestUtils {


    private const val TAG = "TestUtils"


    fun Context.obtainIconsIds(@ArrayRes id: Int): IntArray? {
        var typedArray: TypedArray? = null
        try {
            typedArray = resources.obtainTypedArray(id)
            val ids = mutableListOf<Int>()
            for (i in 0 until typedArray.length()) {
                val resID = typedArray.getResourceId(i, 0)
                if (resID != 0) ids.add(resID)
            }
            return ids.toIntArray()
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
        return null
    }

    fun Context.obtainColors(@ArrayRes id: Int): IntArray? {
        var typedArray: TypedArray? = null
        try {
            typedArray = resources.obtainTypedArray(id)
            val colors = mutableListOf<Int>()
            for (i in 0 until typedArray.length()) {
                val color = typedArray.getColor(i, 0)
                if (color != 0) colors.add(color)
            }
            return colors.toIntArray()
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
        return null
    }

    fun Float.pixelsToDp(context: Context): Float {
        return this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}