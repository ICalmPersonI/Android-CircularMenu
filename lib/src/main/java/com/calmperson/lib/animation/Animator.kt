package com.calmperson.lib.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.calmperson.lib.CircularMenu
import com.calmperson.lib.model.AnimationProperties
import com.calmperson.lib.model.GeometricProperties

internal class Animator(
    private val geometricProperties: GeometricProperties,
    private val animationProperties: AnimationProperties,
    private val setClickable: (Boolean) -> Unit,
    private val invalidate: () -> Unit,
    private val getNumberOfSectors: () -> Int,
) {

    var animationEventListener: CircularMenu.AnimationEventListener = object : CircularMenu.AnimationEventListener { }

    private var currentAnimation: AnimationChain? = null

    fun startOpenAnimation() {
        cancelAnimations()
        animationProperties.cascadeAnimationMode = true
        val animation = createOpenAnimation(
            onStart = {
                animationEventListener.onOpenMenuAnimationStart()
                animationProperties.animatedSectorIndex = getNumberOfSectors()
                setClickable(false)
            },
            onEnd = {
                animationEventListener.onOpenMenuAnimationEnd()
                setClickable(true)
            },
            onCancel = {
                animationEventListener.onOpenMenuAnimationCancel()
                setClickable(true)
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun startCloseAnimation() {
        cancelAnimations()
        animationProperties.cascadeAnimationMode = true
        val animation = createCloseAnimation(
            onStart = {
                animationEventListener.onCloseMenuAnimationStart()
                animationProperties.animatedSectorIndex = 0
                setClickable(false)
            },
            onEnd = {
                animationEventListener.onCloseMenuAnimationEnd()
                setClickable(true)
            },
            onCancel = {
                animationEventListener.onCloseMenuAnimationCancel()
                setClickable(true)
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun startChangeSectorsAnimation(onCollapseSectorsAnimationEnd: () -> Unit) {
        cancelAnimations()
        animationProperties.cascadeAnimationMode = true
        val collapseSectorsAnimation = AnimationChain(
            *createCollapseSectorsAnimation(),
            onStart = {
                animationEventListener.onChangeSectorsAnimationStart()
                animationProperties.animatedSectorIndex = 0
                setClickable(false)
            },
            onCancel = {
                animationEventListener.onChangeSectorsAnimationCancel()
                setClickable(true)
            }
        ).apply {
            addOnEndListener {
                onCollapseSectorsAnimationEnd.invoke()
                val expandSectorsAnimation = AnimationChain(
                    *createExpandSectorsAnimation(),
                    onStart = {
                        animationProperties.animatedSectorIndex = getNumberOfSectors()
                    },
                    onCancel = {
                        animationEventListener.onChangeSectorsAnimationCancel()
                        setClickable(true)
                    },
                ).apply {
                    addOnEndListener {
                        animationEventListener.onChangeSectorsAnimationEnd()
                        setClickable(true)
                    }
                }
                currentAnimation = expandSectorsAnimation
                expandSectorsAnimation.start()
            }
        }

        currentAnimation = collapseSectorsAnimation
        collapseSectorsAnimation.start()
    }

    fun startPressSectorAnimation(sectorIndex: Int) {
        cancelAnimations()
        animationProperties.cascadeAnimationMode = false
        val animation = createPressSectorAnimation(
            onStart = {
                animationProperties.animatedSectorIndex = sectorIndex
                animationEventListener.onPressSectorAnimationStart()
            },
            onEnd = {
                animationEventListener.onPressSectorAnimationEnd()
            },
            onCancel = {
                animationEventListener.onPressSectorAnimationCancel()
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun startReleaseSectorAnimation(sectorIndex: Int) {
        cancelAnimations()
        animationProperties.cascadeAnimationMode = false
        val animation = createReleaseSectorAnimation(
            onStart = {
                animationProperties.animatedSectorIndex = sectorIndex
                animationEventListener.onReleaseSectorAnimationStart()
            },
            onEnd = {
                animationEventListener.onReleaseSectorAnimationEnd()
            },
            onCancel = {
                animationEventListener.onReleaseSectorAnimationCancel()
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun startPressCenterButtonAnimation() {
        animationProperties.cascadeAnimationMode = false
        val animation = createPressCenterButtonAnimation(
            onStart = { animationEventListener.onPressCenterButtonAnimationStart() },
            onEnd = {
                animationEventListener.onPressCenterButtonAnimationEnd()
            },
            onCancel = {
                animationEventListener.onPressCenterButtonAnimationCancel()
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun startReleaseCenterButtonAnimation() {
        val animation = createReleaseCenterButtonAnimation(
            onStart = { animationEventListener.onReleaseCenterButtonAnimationStart() },
            onEnd = {
                animationEventListener.onReleaseCenterButtonAnimationEnd()
            },
            onCancel = {
                animationEventListener.onReleaseCenterButtonAnimationCancel()
            }
        )
        currentAnimation = animation
        animation.start()
    }

    fun cancelAnimations() {
        currentAnimation?.cancel {
            currentAnimation = null
            animationProperties.restoreDefaultState()
            invalidate()
        }
    }

    private fun createPressCenterButtonAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { }
    ): AnimationChain = with(geometricProperties) {
        val animation = createValueAnimator(
            values = intArrayOf(0, innerRadius.toInt() / 5),
            duration = animationProperties.pressCenterButtonAnimationDuration,
            interpolator = LinearInterpolator(),
            updateListener = { value ->
                animationProperties.innerRadiusOffset = value
                invalidate()
            },
        )
        return AnimationChain(
            animation,
            onStart = onStart,
            onCancel = onCancel
        ).apply { addOnEndListener(onEnd) }
    }

    private fun createReleaseCenterButtonAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { }
    ): AnimationChain = with(geometricProperties) {
        val animation = createValueAnimator(
            values = intArrayOf(0, innerRadius.toInt() / 5),
            duration = animationProperties.releaseCenterButtonAnimationDuration,
            interpolator = ReverseInterpolator(),
            updateListener = { value ->
                animationProperties.innerRadiusOffset = value
                invalidate()
            },
        )
        return AnimationChain(
            animation,
            onStart = onStart,
            onCancel = onCancel
        ).apply { addOnEndListener(onEnd) }
    }

    private fun createPressSectorAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { }
    ): AnimationChain = with(geometricProperties) {
        val endAngleOffset = (360 / getNumberOfSectors()) / 15
        val endOuterRadiusOffset = (outerRadius - innerRadius).toInt() / 15
        return AnimationChain(
            createCollapseSectorAnimation(
                duration = animationProperties.pressSectorAnimationDuration,
                startAngleOffset = 0,
                endAngleOffset = endAngleOffset,
                startOuterRadiusOffset = 0,
                endOuterRadiusOffset = endOuterRadiusOffset
            ),
            onStart = onStart,
            onCancel = onCancel,
        ).apply { addOnEndListener { onEnd() } }
    }

    private fun createReleaseSectorAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { }
    ): AnimationChain = with(geometricProperties) {
        val endAngleOffset = (360 / getNumberOfSectors()) / 15
        val endOuterRadiusOffset = (outerRadius - innerRadius).toInt() / 15
        return AnimationChain(
            createExpandSectorAnimation(
                duration = animationProperties.releaseSectorAnimationDuration,
                startAngleOffset = 0,
                endAngleOffset = endAngleOffset,
                startOuterRadiusOffset = 0,
                endOuterRadiusOffset = endOuterRadiusOffset
            ),
            onStart = onStart,
            onCancel = onCancel,
        ).apply { addOnEndListener { onEnd() } }
    }

    private fun createOpenAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { }
    ): AnimationChain = AnimationChain(
        createExpandCenterButtonAnimation(),
        *createExpandSectorsAnimation(),
        onStart = onStart,
        onCancel = onCancel,
    ).apply { addOnEndListener(onEnd) }


    private fun createCloseAnimation(
        onStart: () -> Unit = { },
        onEnd: () -> Unit = { },
        onCancel: () -> Unit = { },
    ): AnimationChain = AnimationChain(
        *createCollapseSectorsAnimation(),
        createCollapseCenterButtonAnimation(),
        onStart = onStart,
        onCancel = onCancel,
    ).apply { addOnEndListener(onEnd) }

    private fun createCollapseSectorsAnimation(): Array<AnimatorSet> = with(geometricProperties) {
        val numberOfSectors = getNumberOfSectors()
        val duration = animationProperties.collapseSectorAnimationDuration / numberOfSectors
        val centerOfAngle = (360 / numberOfSectors) / 2
        val distanceBetweenRadii = (outerRadius - innerRadius).toInt()
        return Array(numberOfSectors) {
            createCollapseSectorAnimation(
                onEnd = { animationProperties.animatedSectorIndex++ },
                duration = duration,
                startAngleOffset = 0,
                endAngleOffset = centerOfAngle,
                startOuterRadiusOffset = 0,
                endOuterRadiusOffset = distanceBetweenRadii
            )
        }
    }


    private fun createExpandSectorsAnimation(): Array<AnimatorSet> = with(geometricProperties) {
        val numberOfSectors = getNumberOfSectors()
        val duration = animationProperties.expandSectorAnimationDuration / numberOfSectors
        val centerOfAngle = (360 / numberOfSectors) / 2
        val distanceBetweenRadii = (outerRadius - innerRadius).toInt()
        return Array(numberOfSectors) {
            createExpandSectorAnimation(
                onStart = { animationProperties.animatedSectorIndex-- },
                duration = duration,
                startAngleOffset = 0,
                endAngleOffset = centerOfAngle,
                startOuterRadiusOffset = 0,
                endOuterRadiusOffset = distanceBetweenRadii
            )
        }
    }


    private fun createCollapseSectorAnimation(
        onStart: (Animator) -> Unit = { },
        onEnd: (Animator) -> Unit = { },
        onCancel: (Animator) -> Unit = { },
        duration: Long,
        startAngleOffset: Int,
        endAngleOffset: Int,
        startOuterRadiusOffset: Int,
        endOuterRadiusOffset: Int,
    ): AnimatorSet = with(animationProperties) {
        val collapseSectorAnimationAngleOffset = createValueAnimator(
            values = intArrayOf(startAngleOffset, endAngleOffset),
            duration = duration,
            interpolator = LinearInterpolator(),
            updateListener = { value -> angleOffset = value },
        )

        val collapseSectorAnimationRadiusOffset = createValueAnimator(
            values = intArrayOf(startOuterRadiusOffset, endOuterRadiusOffset),
            duration = duration,
            interpolator = LinearInterpolator(),
            updateListener = { value ->
                outerRadiusOffset = value
                invalidate()
            },
        )

        return AnimatorSet().apply {
            playTogether(
                collapseSectorAnimationAngleOffset,
                collapseSectorAnimationRadiusOffset
            )
            addListener(onStart = onStart, onEnd = onEnd, onCancel = onCancel)
        }
    }

    private fun createExpandSectorAnimation(
        onStart: (Animator) -> Unit = { },
        onEnd: (Animator) -> Unit = { },
        onCancel: (Animator) -> Unit = { },
        duration: Long,
        startAngleOffset: Int,
        endAngleOffset: Int,
        startOuterRadiusOffset: Int,
        endOuterRadiusOffset: Int,
    ): AnimatorSet = with(animationProperties) {
        val expandSectorAnimationAngleOffset = createValueAnimator(
            values = intArrayOf(startAngleOffset, endAngleOffset),
            duration = duration,
            interpolator = ReverseInterpolator(),
            updateListener = { value -> angleOffset = value },
        )

        val expandSectorAnimationRadiusOffset = createValueAnimator(
            values = intArrayOf(startOuterRadiusOffset, endOuterRadiusOffset),
            duration = duration,
            interpolator = ReverseInterpolator(),
            updateListener = { value ->
                outerRadiusOffset = value
                invalidate()
            },
        )
        return AnimatorSet().apply {
            playTogether(
                expandSectorAnimationAngleOffset,
                expandSectorAnimationRadiusOffset
            )
            addListener(onStart = onStart, onEnd = onEnd, onCancel = onCancel)
        }
    }

    private fun createCollapseCenterButtonAnimation(
        onStart: () -> Unit = { },
        onCancel: () -> Unit = { },
        onEnd: () -> Unit = { }
    ): AnimationChain = with(animationProperties) {
        val collapseCenterButtonAnimation = createValueAnimator(
            values = intArrayOf(0, geometricProperties.innerRadius.toInt()),
            duration = collapseCenterButtonDuration,
            interpolator = LinearInterpolator(),
            updateListener = { value ->
                innerRadiusOffset = value
                invalidate()
            },
        )

        val rotateCenterButtonAnimation = createSpringAnimation(
            startPosition = startCenterButtonRotatePosition,
            finalPosition = endCenterButtonRotatePosition,
            updateListener = { value ->
                centerButtonRotation = value
                invalidate()
            },
        )

        return AnimationChain(
            rotateCenterButtonAnimation,
            collapseCenterButtonAnimation,
            onStart = onStart,
            onCancel = onCancel,
        ).apply { addOnEndListener(onEnd) }
    }

    private fun createExpandCenterButtonAnimation(
        onStart: () -> Unit = { },
        onCancel: () -> Unit = { },
        onEnd: () -> Unit = { }
    ): AnimationChain = with(animationProperties) {
        val expandCenterButtonAnimation = createValueAnimator(
            values = intArrayOf(0, geometricProperties.innerRadius.toInt()),
            duration = expandCenterButtonDuration,
            interpolator = ReverseInterpolator(),
            updateListener = { value ->
                innerRadiusOffset = value
                invalidate()
            },
        )

        val rotateCenterButtonAnimation = createSpringAnimation(
            startPosition = endCenterButtonRotatePosition,
            finalPosition = startCenterButtonRotatePosition,
            updateListener = { value ->
                centerButtonRotation = value
                invalidate()
            },
        )

        return AnimationChain(
            expandCenterButtonAnimation,
            rotateCenterButtonAnimation,
            onStart = onStart,
            onCancel = onCancel,
        ).apply { addOnEndListener(onEnd) }
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