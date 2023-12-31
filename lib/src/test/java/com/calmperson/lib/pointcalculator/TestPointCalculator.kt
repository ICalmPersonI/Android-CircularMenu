package com.calmperson.lib.pointcalculator

import com.calmperson.lib.PointCalculator
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class TestPointCalculator {

    companion object {
        private const val NUMBER_OF_POINTS = 10_000_000
    }

    private var numberOfSectors: Int = 0
    private lateinit var sectors: Array<Sector>
    private lateinit var pointCalculator: PointCalculator

    @Before
    fun before() {
        initSectors()
        pointCalculator = PointCalculator()
    }

    private fun initSectors() {
        numberOfSectors = 6
        sectors = Array(numberOfSectors) { i ->
            val sizeOfSector = 360 / numberOfSectors
            Sector(startAngle = sizeOfSector * i, endAngle = sizeOfSector * (i + 1))
        }
    }

    @Test
    fun testIsPointInSector_generatePointsInSector() = sectors.forEach { sector ->
        val height = Random.nextInt(10, 1000)
        val width = Random.nextInt(10, 1000)
        val centerX = height / 2f
        val centerY = width / 2f
        val outerRadius = min(width, height) / 2f
        val innerRadius = outerRadius / 2f

        val radiusFrom = innerRadius.toDouble()
        val radiusTo = outerRadius.toDouble()
        repeat(NUMBER_OF_POINTS) {
            val (x, y) = generateRandomPoint(
                startAngle = sector.startAngle,
                endAngle = sector.endAngle,
                radiusFrom = radiusFrom,
                radiusTo = radiusTo,
                centerX = centerX,
                centerY = centerY
            )
            Assert.assertTrue(
                pointCalculator.isPontInSector(
                    x = x,
                    y = y,
                    startAngle = sector.startAngle,
                    endAngle = sector.endAngle,
                    centerX = centerX,
                    centerY = centerY,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius
                )
            )
        }
    }


    @Test
    fun testIsPointInSector_generatePointsOutOfSectorOutOuterRadius() = sectors.forEach { sector ->
        val height = Random.nextInt(10, 1000)
        val width = Random.nextInt(10, 1000)
        val centerX = height / 2f
        val centerY = width / 2f
        val outerRadius = min(width, height) / 2f
        val innerRadius = outerRadius / 2f

        val radiusFrom: Double = outerRadius.toDouble() + 1
        val radiusTo: Double = max(width, height).toDouble()
        repeat(NUMBER_OF_POINTS) {
            val (x, y) = generateRandomPoint(
                startAngle = sector.startAngle,
                endAngle = sector.endAngle,
                radiusFrom = radiusFrom,
                radiusTo = radiusTo,
                centerX = centerX,
                centerY = centerY
            )
            Assert.assertFalse(
                pointCalculator.isPontInSector(
                    x = x,
                    y = y,
                    startAngle = sector.startAngle,
                    endAngle = sector.endAngle,
                    centerX = centerX,
                    centerY = centerY,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius
                )
            )
        }
    }


    @Test
    fun testIsPointInSector_generatePointsOutOfSectorInsideInnerRadius() {
        sectors.forEach { sector ->
            val height = Random.nextInt(10, 1000)
            val width = Random.nextInt(10, 1000)
            val centerX = height / 2f
            val centerY = width / 2f
            val outerRadius = min(width, height) / 2f
            val innerRadius = outerRadius / 2f

            val radiusFrom: Double = 0.0
            val radiusTo: Double = innerRadius.toDouble() - 1
            repeat(NUMBER_OF_POINTS) {
                val (x, y) = generateRandomPoint(
                    startAngle = sector.startAngle,
                    endAngle = sector.endAngle,
                    radiusFrom = radiusFrom,
                    radiusTo = radiusTo,
                    centerX = centerX,
                    centerY = centerY
                )
                Assert.assertFalse(
                    pointCalculator.isPontInSector(
                        x = x,
                        y = y,
                        startAngle = sector.startAngle,
                        endAngle = sector.endAngle,
                        centerX = centerX,
                        centerY = centerY,
                        outerRadius = outerRadius,
                        innerRadius = innerRadius
                    )
                )
            }
        }
    }


    @Test
    fun testIsPointInCenterButton_generatePointInCenterButtonRadius() {
        val height = Random.nextInt(10, 1000)
        val width = Random.nextInt(10, 1000)
        val centerX = height / 2f
        val centerY = width / 2f
        val outerRadius = min(width, height) / 2f
        val innerRadius = outerRadius / 2f
        val centerButtonPadding = 10f

        val from = 0.0
        val to = innerRadius.toDouble() - centerButtonPadding
        repeat(NUMBER_OF_POINTS) {
            val (x, y) = generateRandomPoint(from, to, 0, 360, centerX, centerY)
            Assert.assertTrue(
                pointCalculator.isPointInCenterButton(
                    x = x,
                    y = y,
                    centerX = centerX,
                    centerY = centerY,
                    innerRadius = innerRadius,
                    centerButtonPadding = centerButtonPadding
                )
            )
        }
    }

    @Test
    fun testIsPointInCenterButton_generatePointOutOfCenterButtonRadius() {
        val height = Random.nextInt(10, 1000)
        val width = Random.nextInt(10, 1000)
        val centerX = height / 2f
        val centerY = width / 2f
        val outerRadius = min(width, height) / 2f
        val innerRadius = outerRadius / 2f
        val centerButtonPadding = 10f

        val from = innerRadius.toDouble() - centerButtonPadding + 1
        val to = min(width, height).toDouble()
        repeat(NUMBER_OF_POINTS) {
            val (x, y) = generateRandomPoint(from, to, 0, 360, centerX, centerY)
            Assert.assertFalse(
                pointCalculator.isPointInCenterButton(
                    x = x,
                    y = y,
                    centerX = centerX,
                    centerY = centerY,
                    innerRadius = innerRadius,
                    centerButtonPadding = centerButtonPadding
                )
            )
        }
    }

    private fun generateRandomPoint(
        radiusFrom: Double,
        radiusTo: Double,
        startAngle: Int,
        endAngle: Int,
        centerX: Float,
        centerY: Float
    ): Pair<Float, Float> {
        val randomRadius = Random.nextDouble(radiusFrom, radiusTo)
        val randomAngle = Math.toRadians(Random.nextInt(startAngle, endAngle).toDouble())

        val x = (centerX + randomRadius * cos(randomAngle)).toFloat()
        val y = (centerY + randomRadius * sin(randomAngle)).toFloat()

        return Pair(x, y)
    }

}