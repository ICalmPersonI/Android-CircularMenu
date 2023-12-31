package com.calmperson.circularmenu

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.ArrayRes
import com.calmperson.lib.CircularMenu

class MyCircularMenu(
    context: Context,
    @ArrayRes iconsArrayId: Int
) : CircularMenu(context, iconsArrayId) {

    companion object {
        private val TAG = MyCircularMenu::class.simpleName
    }

    private var lastPressedSectorIndex: Int? = null
    private var isCenterButtonPressed: Boolean = false

    init {
        setAnimationEventListener(
            object : AnimationEventListener {
                override fun onCloseMenuAnimationEnd() {
                    //removeThisViewFromParent()
                }
            }
        )
    }

    override var onCenterButtonClick: (CircularMenu) -> Unit = {
        startCloseAnimation()
        Log.i(TAG, "Center button click.")
    }

    override var onSectorClick: (CircularMenu, sectorIndex: Int) -> Unit = { view, i ->
        Log.i(TAG, "Sector $i.")
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        val action = event?.action

        if (x == null || y == null || action == null || !isClickable) return false

        if (isPointInCenterButton(x, y)) {
            handleOutOfSectorsBoundsTouch()
            return handleOnCenterButtonClick(action)
        } else {
            if (isCenterButtonPressed) releaseCenterButton()
            getSectorIndex(x, y)?.let { sectorIndex ->
                return handleOnSectorClick(sectorIndex, action)
            } ?: run(::handleOutOfSectorsBoundsTouch)
        }
        return super.onTouchEvent(event)
    }


    private fun handleOnSectorClick(currentIndex: Int, action: Int): Boolean {
        val result = when (action) {
            MotionEvent.ACTION_DOWN -> {
                pressSector(currentIndex)
                lastPressedSectorIndex = currentIndex
                true
            }
            MotionEvent.ACTION_UP -> {
                releaseSector(currentIndex)
                lastPressedSectorIndex = null
                onSectorClick(this, currentIndex)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                lastPressedSectorIndex?.let { i ->
                    if (i != currentIndex) {
                        releaseSector(i)
                        pressSector(currentIndex)
                    }
                } ?: run { pressSector(currentIndex) }
                lastPressedSectorIndex = currentIndex
                true

            }
            else -> false
        }
        invalidate()
        return result
    }

    private fun handleOnCenterButtonClick(action: Int): Boolean {
        val pressedColor = centerButtonPressedColor
        val defaultColor = centerButtonColor
        val result = when (action) {
            MotionEvent.ACTION_DOWN -> {
                startPressCenterButtonAnimation()
                setCenterButtonCurrentColor(pressedColor)
                isCenterButtonPressed = true
                true
            }
            MotionEvent.ACTION_UP -> {
                startReleaseCenterButtonAnimation()
                setCenterButtonCurrentColor(defaultColor)
                isCenterButtonPressed = false
                onCenterButtonClick.invoke(this)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isCenterButtonPressed) {
                    startPressCenterButtonAnimation()
                    isCenterButtonPressed = true
                    setCenterButtonCurrentColor(pressedColor)
                }
                true
            }
            else -> false
        }
        invalidate()
        return result
    }

    private fun releaseSector(sectorIndex: Int) {
        startReleaseSectorAnimation(sectorIndex)
        sectors[sectorIndex].currentColor = sectors[sectorIndex].color
    }

    private fun pressSector(sectorIndex: Int) {
        startPressSectorAnimation(sectorIndex)
        sectors[sectorIndex].currentColor = sectors[sectorIndex].pressedColor
    }

    private fun releaseCenterButton() {
        startReleaseCenterButtonAnimation()
        isCenterButtonPressed = false
        setCenterButtonCurrentColor(centerButtonColor)
    }

    private fun handleOutOfSectorsBoundsTouch() {
        lastPressedSectorIndex?.let { i -> releaseSector(i) }
        lastPressedSectorIndex = null
    }

}