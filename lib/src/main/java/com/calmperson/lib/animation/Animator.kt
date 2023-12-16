package com.calmperson.lib.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.calmperson.lib.CircularMenu
import com.calmperson.lib.Drawer

internal class Animator(private val circularMenu: CircularMenu, private val drawer: Drawer) {

    companion object {
        private const val COLLAPSE_SECTORS_DURATION = 100L
        private const val COLLAPSE_CENTER_BUTTON_DURATION = 600L
        private const val EXPAND_SECTORS_DURATION = 100L
        private const val EXPAND_CENTER_BUTTON_DURATION = 600L
        private const val START_CENTER_BUTTON_ROTATE_POSITION = 0f
        private const val END_CENTER_BUTTON_ROTATE_POSITION = 180f
    }

    var animationEventListener = object : CircularMenu.AnimationEventListener {
        override fun onOpenMenuAnimationStart() {}
        override fun onOpenMenuAnimationEnd() {}
        override fun onOpenMenuAnimationCancel() {}
        override fun onCloseMenuAnimationStart() {}
        override fun onCloseMenuAnimationEnd() {}
        override fun onCloseMenuAnimationCancel() {}
    }

    private var currentAnimatorChain: AnimationChain? = null

    fun startOpenAnimation() {
        cancelAnimations()
        val animation = createCloseAnimation(
            reverse = true,
            onStart = {
                animationEventListener.onCloseMenuAnimationStart()
                drawer.animatedSectorIndex = circularMenu.numberOfSectors
                circularMenu.isClickable = false
            }
        )
        currentAnimatorChain = animation
        animation.start()
    }

    fun startCloseAnimation() {
        cancelAnimations()
        val animation = createCloseAnimation(
            onStart = {
                animationEventListener.onCloseMenuAnimationStart()
                drawer.animatedSectorIndex = 0
                circularMenu.isClickable = false
            }
        )
        currentAnimatorChain = animation
        animation.start()
    }

    private fun cancelAnimations() {
        currentAnimatorChain?.cancel()
        currentAnimatorChain = null
    }

    private fun createCloseAnimation(
        reverse: Boolean = false,
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {
            if (reverse) animationEventListener.onOpenMenuAnimationEnd()
            else animationEventListener.onCloseMenuAnimationEnd()
            circularMenu.isClickable = true
        },
        onCancel: () -> Unit = {
            if (reverse) animationEventListener.onOpenMenuAnimationCancel()
            else animationEventListener.onCloseMenuAnimationCancel()
            circularMenu.isClickable = true
        },
    ): AnimationChain = with(drawer) {
        val centerOfAngle = (360 / circularMenu.numberOfSectors) / 2
        val expandSectorsAnimationAngleOffset = createValueAnimator(
            values = intArrayOf(0, centerOfAngle),
            duration = if (reverse) EXPAND_SECTORS_DURATION else COLLAPSE_SECTORS_DURATION,
            interpolator = if (reverse) ReverseInterpolator() else LinearInterpolator(),
            updateListener = { value -> angleOffset = value },
        )

        val distanceBetweenRadii = (outerRadius - innerRadius).toInt()
        val expandSectorsAnimationRadiusOffset = createValueAnimator(
            values = intArrayOf(0, distanceBetweenRadii),
            duration = if (reverse) EXPAND_SECTORS_DURATION else COLLAPSE_SECTORS_DURATION,
            interpolator = if (reverse) ReverseInterpolator() else LinearInterpolator(),
            updateListener = { value -> outerRadiusOffset = value; circularMenu.invalidate() },
        )

        val createSet = {
            AnimatorSet().apply {
                playTogether(
                    expandSectorsAnimationAngleOffset,
                    expandSectorsAnimationRadiusOffset
                )
                addListener(
                    onEnd = {
                        if (reverse) animatedSectorIndex--
                        else animatedSectorIndex++
                    }
                )
            }
        }

        val collapseCenterButtonAnimation = createValueAnimator(
            values = intArrayOf(0, innerRadius.toInt()),
            duration = if (reverse) EXPAND_CENTER_BUTTON_DURATION else COLLAPSE_CENTER_BUTTON_DURATION,
            interpolator = if (reverse) ReverseInterpolator() else LinearInterpolator(),
            updateListener = { value -> innerRadiusOffset = value; circularMenu.invalidate() },
        )

        val rotateCenterButtonAnimation = createSpringAnimation(
            startPosition = if (reverse) END_CENTER_BUTTON_ROTATE_POSITION else START_CENTER_BUTTON_ROTATE_POSITION,
            finalPosition = if (reverse) START_CENTER_BUTTON_ROTATE_POSITION else END_CENTER_BUTTON_ROTATE_POSITION,
            updateListener = { value -> circularMenu.rotation = value; circularMenu.invalidate() },
        )

        return if (reverse) {
            AnimationChain(
                collapseCenterButtonAnimation,
                rotateCenterButtonAnimation,
                *Array(circularMenu.numberOfSectors) { createSet.invoke() },
                onStart = onStart,
                onEnd = onEnd,
                onCancel = onCancel,
            )
        } else {
            AnimationChain(
                *Array(circularMenu.numberOfSectors) { createSet.invoke() },
                rotateCenterButtonAnimation,
                collapseCenterButtonAnimation,
                onStart = onStart,
                onEnd = onEnd,
                onCancel = onCancel,
            )
        }
    }


    private fun createValueAnimator(
        vararg values: Int,
        duration: Long,
        interpolator: Interpolator,
        updateListener: (Float) -> Unit,
    ): ValueAnimator {
        val animator = ValueAnimator.ofInt(*values).apply {
            this.duration = duration
            this.interpolator = interpolator
            addUpdateListener { valueAnimator ->
                updateListener.invoke((valueAnimator.animatedValue as Int).toFloat())
            }
        }
        return animator
    }


    private fun createSpringAnimation(
        startPosition: Float,
        finalPosition: Float,
        updateListener: (Float) -> Unit,
    ): SpringAnimation {
        val springForce = SpringForce().apply {
            this.finalPosition = finalPosition
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        }

        val animation = SpringAnimation(FloatValueHolder(startPosition)).apply {
            spring = springForce
            addUpdateListener { _, value, _ -> updateListener.invoke(value) }
        }
        return animation
    }


}