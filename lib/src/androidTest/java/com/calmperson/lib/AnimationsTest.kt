package com.calmperson.lib

import android.content.Context
import android.os.Handler
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.calmperson.lib.animation.Animator
import com.calmperson.lib.model.AnimationProperties
import com.calmperson.lib.model.GeometricProperties
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import kotlin.math.min
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class AnimationsTest {

    private lateinit var instrumentationContext: Context
    private lateinit var animator: Animator
    private lateinit var animationProperties: AnimationProperties
    private lateinit var geometricProperties: GeometricProperties
    private var numberOfSectors = 0

    @Before
    fun before() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        initProperties(circularMenuSize = Random.nextInt(10, 1000))
        numberOfSectors = Random.nextInt(2, 10)
    }

    private fun initProperties(circularMenuSize: Int) {

        val centerX = circularMenuSize / 2f
        val centerY = circularMenuSize / 2f
        val outerRadius = min(circularMenuSize, circularMenuSize) / 2f
        val innerRadius = outerRadius / 2f
        val outerRectFLeft = centerX - outerRadius
        val outerRectFTop = centerY - outerRadius
        val outerRectFRight = centerX + outerRadius
        val outerRectFBottom = centerY + outerRadius

        geometricProperties = GeometricProperties(
            centerX = centerX,
            centerY = centerY,
            outerRadius = outerRadius,
            innerRadius = innerRadius,
            outerRectFLeft = outerRectFLeft,
            outerRectFTop = outerRectFTop,
            outerRectFRight = outerRectFRight,
            outerRectFBottom = outerRectFBottom,
            sectorSpacing = 0f,
            centerButtonPadding = 0f
        )

        animationProperties = AnimationProperties()
    }

    private fun initAnimator(numberOfSectors: Int, invalidate: () -> Unit) {
        animator = Animator(
            geometricProperties = geometricProperties,
            animationProperties = animationProperties,
            setClickable = { _ -> },
            invalidate = invalidate,
            getNumberOfSectors = { numberOfSectors }

        )
    }


    @Test
    fun testOpenAnimation_startToEnd() {
        with(animationProperties) {
            val centerOfAngle = (360 / numberOfSectors) / 2f
            val distanceBetweenRadii =
                (geometricProperties.outerRadius - geometricProperties.innerRadius)

            initAnimator(
                numberOfSectors = numberOfSectors,
                invalidate = {
                    System.err.println(angleOffset)
                    if (animatedSectorIndex !in 0..numberOfSectors) assertFalse(true)
                    if (angleOffset !in 0f..centerOfAngle) assertFalse(true)
                    if (outerRadiusOffset !in 0f..distanceBetweenRadii) assertFalse(true)
                    if (innerRadiusOffset !in 0f..geometricProperties.innerRadius) {
                        assertFalse(true)
                    }
                    if (centerButtonRotation !in startCenterButtonRotatePosition
                        ..endCenterButtonRotatePosition
                    ) {
                        assertFalse(true)
                    }

                },
            )

            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                    override fun onOpenMenuAnimationStart() {
                        animationStarted = true
                    }

                    override fun onOpenMenuAnimationEnd() {
                        animationEnded = true
                        latch.countDown()
                    }

                    override fun onOpenMenuAnimationCancel() {
                        animationCanceled = true
                        latch.countDown()
                    }
                }
                animator.startOpenAnimation()
            }

            latch.await()
            assertTrue(animationStarted && animationEnded && !animationCanceled)

        }
    }

    @Test
    fun testOpenAnimation_cancelAnimation() {
        initAnimator(
            numberOfSectors = numberOfSectors,
            invalidate = {},
        )

        var animationStarted = false
        var animationEnded = false
        var animationCanceled = false

        val latch = CountDownLatch(1)
        Handler(instrumentationContext.mainLooper).post {
            animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                override fun onOpenMenuAnimationStart() {
                    animationStarted = true
                }

                override fun onOpenMenuAnimationEnd() {
                    animationEnded = true
                    latch.countDown()
                }

                override fun onOpenMenuAnimationCancel() {
                    animationCanceled = true
                    latch.countDown()
                }
            }
            animator.startOpenAnimation()
            animator.cancelAnimations()
        }

        latch.await()
        assertTrue(animationStarted && !animationEnded && animationCanceled)
    }


    @Test
    fun testCloseAnimation_startToEnd() {
        with(animationProperties) {

            val centerOfAngle = (360 / numberOfSectors) / 2f
            val distanceBetweenRadii =
                (geometricProperties.outerRadius - geometricProperties.innerRadius)

            initAnimator(
                numberOfSectors = numberOfSectors,
                invalidate = {
                    if (animatedSectorIndex !in 0..numberOfSectors) assertFalse(true)
                    if (angleOffset !in 0f..centerOfAngle) assertFalse(true)
                    if (outerRadiusOffset !in 0f..distanceBetweenRadii) assertFalse(true)
                    if (innerRadiusOffset !in 0f..geometricProperties.innerRadius) {
                        assertFalse(true)
                    }
                    if (centerButtonRotation !in startCenterButtonRotatePosition
                        ..endCenterButtonRotatePosition) {
                        assertFalse(true)
                    }
                },
            )

            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener =
                    object : CircularMenu.AnimationEventListener {
                        override fun onCloseMenuAnimationStart() {
                            animationStarted = true
                        }

                        override fun onCloseMenuAnimationEnd() {
                            animationEnded = true
                            latch.countDown()
                        }

                        override fun onCloseMenuAnimationCancel() {
                            animationCanceled = true
                            latch.countDown()
                        }
                    }
                animator.startCloseAnimation()
            }

            latch.await()
            assertTrue(animationStarted && animationEnded && !animationCanceled)

        }
    }

    @Test
    fun testCloseAnimation_cancelAnimation() {
        initAnimator(
            numberOfSectors = numberOfSectors,
            invalidate = {},
        )

        var animationStarted = false
        var animationEnded = false
        var animationCanceled = false

        val latch = CountDownLatch(1)
        Handler(instrumentationContext.mainLooper).post {
            animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                override fun onCloseMenuAnimationStart() {
                    animationStarted = true
                }

                override fun onCloseMenuAnimationEnd() {
                    animationEnded = true
                    latch.countDown()
                }

                override fun onCloseMenuAnimationCancel() {
                    animationCanceled = true
                    latch.countDown()
                }
            }
            animator.startCloseAnimation()
            animator.cancelAnimations()
        }

        latch.await()
        assertTrue(animationStarted && !animationEnded && animationCanceled)
    }

    @Test
    fun testChangeSectorsAnimation_startToEnd() {
        with(animationProperties) {
            val centerOfAngle = (360 / numberOfSectors) / 2f
            val distanceBetweenRadii =
                (geometricProperties.outerRadius - geometricProperties.innerRadius)

            initAnimator(
                numberOfSectors = numberOfSectors,
                invalidate = {
                    if (animatedSectorIndex !in 0..numberOfSectors) assertFalse(true)
                    if (angleOffset !in 0f..centerOfAngle) assertFalse(true)
                    if (outerRadiusOffset !in 0f..distanceBetweenRadii) assertFalse(true)
                },
            )

            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false
            var collapseSectorsAnimationEnd = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                    override fun onChangeSectorsAnimationStart() {
                        animationStarted = true
                    }

                    override fun onChangeSectorsAnimationEnd() {
                        animationEnded = true
                        latch.countDown()
                    }

                    override fun onChangeSectorsAnimationCancel() {
                        animationCanceled = true
                        latch.countDown()
                    }
                }
                animator.startChangeSectorsAnimation(
                    onCollapseSectorsAnimationEnd = {
                        collapseSectorsAnimationEnd = true
                    }
                )
            }

            latch.await()
            assertTrue(animationStarted && animationEnded && !animationCanceled && collapseSectorsAnimationEnd)
        }
    }

    @Test
    fun testChangeSectorsAnimation_cancelAnimation() {
        initAnimator(
            numberOfSectors = numberOfSectors,
            invalidate = {},
        )
        var animationStarted = false
        var animationEnded = false
        var animationCanceled = false
        var collapseSectorsAnimationEnd = false

        val latch = CountDownLatch(1)
        Handler(instrumentationContext.mainLooper).post {
            animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                override fun onChangeSectorsAnimationStart() {
                    animationStarted = true
                }

                override fun onChangeSectorsAnimationEnd() {
                    animationEnded = true
                    latch.countDown()
                }

                override fun onChangeSectorsAnimationCancel() {
                    animationCanceled = true
                    latch.countDown()
                }
            }
            animator.startChangeSectorsAnimation(
                onCollapseSectorsAnimationEnd = {
                    collapseSectorsAnimationEnd = true
                }
            )
            animator.cancelAnimations()
        }

        latch.await()
        assertTrue(animationStarted && !animationEnded && animationCanceled && !collapseSectorsAnimationEnd)
    }

    @Test
    fun testPressSectorAnimation_startToEnd() {
        with(animationProperties) {
            val centerOfAngle = (360 / numberOfSectors) / 2f
            val distanceBetweenRadii =
                (geometricProperties.outerRadius - geometricProperties.innerRadius)
            initAnimator(
                numberOfSectors = numberOfSectors,
                invalidate = {
                    if (animatedSectorIndex !in 0..numberOfSectors) assertFalse(true)
                    if (angleOffset !in 0f..centerOfAngle) assertFalse(true)
                    if (outerRadiusOffset !in 0f..distanceBetweenRadii) assertFalse(true)
                },
            )

            val sectorIndex = Random.nextInt(0, numberOfSectors)
            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener =
                    object : CircularMenu.AnimationEventListener {
                        override fun onPressSectorAnimationStart() {
                            animationStarted = true
                        }

                        override fun onPressSectorAnimationEnd() {
                            animationEnded = true
                            latch.countDown()
                        }

                        override fun onPressSectorAnimationCancel() {
                            animationCanceled = true
                            latch.countDown()
                        }
                    }
                animator.startPressSectorAnimation(sectorIndex)
            }

            latch.await()
            assertTrue(animationStarted && animationEnded && !animationCanceled)
        }
    }

    @Test
    fun testPressSectorAnimation_cancelAnimation() {
        initAnimator(
            numberOfSectors = numberOfSectors,
            invalidate = {},
        )

        for (sectorIndex in 0 until numberOfSectors) {
            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                    override fun onPressSectorAnimationStart() {
                        animationStarted = true
                    }

                    override fun onPressSectorAnimationEnd() {
                        animationEnded = true
                        latch.countDown()
                    }

                    override fun onPressSectorAnimationCancel() {
                        animationCanceled = true
                        latch.countDown()
                    }
                }
                animator.startPressSectorAnimation(sectorIndex)
                animator.cancelAnimations()
            }

            latch.await()
            assertTrue(animationStarted && !animationEnded && animationCanceled)
        }
    }

    @Test
    fun testPressCenterButtonAnimation_startToEnd() {
        with(animationProperties) {
            initAnimator(
                numberOfSectors = 5,
                invalidate = {
                    if (animatedSectorIndex != 0) assertFalse(true)
                    if (angleOffset != 0f) assertFalse(true)
                    if (outerRadiusOffset != 0f) assertFalse(true)
                    if (innerRadiusOffset !in 0f..geometricProperties.innerRadius) assertFalse(false)
                    if (centerButtonRotation !in startCenterButtonRotatePosition..endCenterButtonRotatePosition) assertFalse(
                        false
                    )
                },
            )

            var animationStarted = false
            var animationEnded = false
            var animationCanceled = false

            val latch = CountDownLatch(1)
            Handler(instrumentationContext.mainLooper).post {
                animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                    override fun onPressCenterButtonAnimationStart() {
                        animationStarted = true
                    }

                    override fun onPressCenterButtonAnimationEnd() {
                        animationEnded = true
                        latch.countDown()
                    }

                    override fun onPressCenterButtonAnimationCancel() {
                        animationCanceled = true
                        latch.countDown()
                    }
                }
                animator.startPressCenterButtonAnimation()
            }

            latch.await()
            assertTrue(animationStarted && animationEnded && !animationCanceled)
        }
    }

    @Test
    fun testPressCenterButtonAnimation_cancelAnimation() {
        initAnimator(
            numberOfSectors = 5,
            invalidate = { },
        )

        var animationStarted = false
        var animationEnded = false
        var animationCanceled = false

        val latch = CountDownLatch(1)
        Handler(instrumentationContext.mainLooper).post {
            animator.animationEventListener = object : CircularMenu.AnimationEventListener {
                override fun onPressCenterButtonAnimationStart() {
                    animationStarted = true
                }

                override fun onPressCenterButtonAnimationEnd() {
                    animationEnded = true
                    latch.countDown()
                }

                override fun onPressCenterButtonAnimationCancel() {
                    animationCanceled = true
                    latch.countDown()
                }
            }
            animator.startPressCenterButtonAnimation()
            animator.cancelAnimations()
        }

        latch.await()
        assertTrue(animationStarted && !animationEnded && animationCanceled)
    }


}