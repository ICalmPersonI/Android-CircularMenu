package com.calmperson.lib.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.core.animation.addListener
import androidx.dynamicanimation.animation.SpringAnimation

class AnimationChain(
    private vararg val animations: Any,
    private val onStart: () -> Unit,
    private val onEnd: () -> Unit,
    private val onCancel: () -> Unit
) {

    private val iterator = animations.iterator()
    private var cancel = false

    fun start() {
        if (animations.isEmpty()) {
            onEnd()
            return
        }
        onStart()
        play()
    }

    fun cancel() {
        cancel = true
        onCancel()
    }

    private fun play() {
        val animation = iterator.next()
        val onEnd = {
            if (iterator.hasNext() && !cancel) play()
            else onEnd()
        }
        when (animation) {
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
        }
    }
}