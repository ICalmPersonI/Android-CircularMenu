package com.calmperson.lib.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.core.animation.addListener
import androidx.dynamicanimation.animation.SpringAnimation

class AnimationChain(
    private vararg val animations: Any,
    private val onStart: () -> Unit,
    private val onCancel: () -> Unit
) {

    private val onEndListeners = mutableListOf<() -> Unit>()
    private val iterator = animations.iterator()

    private var cancel = false
    private lateinit var currentAnimation: Any

    fun start() {
        onStart()
        if (animations.isEmpty()) {
            onEndListeners.forEach { it.invoke() }
            return
        }
        play()
    }

    fun cancel(doAfterCancel: () -> Unit = { }) {
        cancel = true
        /*
        animations.forEach { animation ->
            when (animation) {
                is AnimatorSet -> animation.cancel()
                is ValueAnimator -> animation.cancel()
                is SpringAnimation -> {
                    animation.cancel()
                    Log.e("Tst", "Cancel")
                }
                is AnimationChain -> animation.cancel()
            }
        }
         */
        when (val animation = currentAnimation) {
            is AnimatorSet ->  animation.cancel()
            is ValueAnimator ->  animation.cancel()
            is SpringAnimation ->  animation.cancel()
            is AnimationChain ->  animation.cancel()
        }
        onCancel()
        doAfterCancel()
    }

    fun addOnEndListener(listener: () -> Unit) {
        onEndListeners.add(listener)
    }

    private fun play() {
        currentAnimation = iterator.next()
        val onEnd = {
            if (iterator.hasNext() && !cancel) play()
            else if (!cancel) onEndListeners.forEach { it.invoke() }
        }
        when (val animation = currentAnimation) {
            is AnimatorSet -> {
                animation.addListener(onEnd = { onEnd() })
                animation.start()
            }

            is ValueAnimator -> {
                animation.addListener(onEnd = { onEnd() })
                animation.start()
            }

            is SpringAnimation -> {
                animation.addEndListener { _, _, _, _ -> onEnd() }
                animation.start()
            }

            is AnimationChain -> {
                animation.addOnEndListener { onEnd() }
                animation.start()
            }
        }
    }
}