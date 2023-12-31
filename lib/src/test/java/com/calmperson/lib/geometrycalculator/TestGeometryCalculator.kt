package com.calmperson.lib.geometrycalculator

import com.calmperson.lib.GeometryCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TestGeometryCalculator {

    private lateinit var geometryCalculator: GeometryCalculator

    @Before
    fun init() {
        geometryCalculator = GeometryCalculator()
    }

    @Test
    fun testCalculateSectorParameters_case1() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 0,
                startAngle = 0,
                endAngle = 20
            ).setAnimationProperties(
                animatedSectorIndex = 0,
                angleOffset = 5f,
                outerRadiusOffset = 10f,
                cascadeAnimationMode = true
            ).setGeometricProperties(
                circularMenuHeight = 100,
                circularMenuWidth = 100,
            ).setExpectValues(
                expectStartAngle = 5f,
                expectSweepAngle = 10f,
                expectLeft = 10f,
                expectTop = 10f,
                expectRight = 90f,
                expectBottom = 90f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_case2() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 0,
                startAngle = 0,
                endAngle = 20
            ).setAnimationProperties(
                animatedSectorIndex = 0,
                angleOffset = 5f,
                outerRadiusOffset = 10f,
                cascadeAnimationMode = false
            ).setGeometricProperties(
                circularMenuHeight = 100,
                circularMenuWidth = 100,
            ).setExpectValues(
                expectStartAngle = 5f,
                expectSweepAngle = 10f,
                expectLeft = 10f,
                expectTop = 10f,
                expectRight = 90f,
                expectBottom = 90f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_case3() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 5,
                startAngle = 40,
                endAngle = 80
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                angleOffset = 20f,
                outerRadiusOffset = 15f,
                cascadeAnimationMode = true
            ).setGeometricProperties(
                circularMenuHeight = 200,
                circularMenuWidth = 200,
            ).setExpectValues(
                expectStartAngle = 40f,
                expectSweepAngle = 40f,
                expectLeft = 0f,
                expectTop = 0f,
                expectRight = 200f,
                expectBottom = 200f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_case4() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 1,
                startAngle = 40,
                endAngle = 80
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                angleOffset = 20f,
                outerRadiusOffset = 15f,
                cascadeAnimationMode = false
            ).setGeometricProperties(
                circularMenuHeight = 200,
                circularMenuWidth = 200,
            ).setExpectValues(
                expectStartAngle = 40f,
                expectSweepAngle = 40f,
                expectLeft = 0f,
                expectTop = 0f,
                expectRight = 200f,
                expectBottom = 200f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_case5() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 6,
                startAngle = 4533,
                endAngle = 5065,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                angleOffset = 1030f,
                outerRadiusOffset = 22000f,
                cascadeAnimationMode = true
            ).setGeometricProperties(
                circularMenuHeight = 22200,
                circularMenuWidth = 22200,
            ).setExpectValues(
                expectStartAngle = 4533f,
                expectSweepAngle = 532f,
                expectLeft = 0f,
                expectTop = 0f,
                expectRight = 22200f,
                expectBottom = 22200f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_case6() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 6,
                startAngle = -44,
                endAngle = -70,
            ).setAnimationProperties(
                animatedSectorIndex = 6,
                angleOffset = 32.4f,
                outerRadiusOffset = 123.3f,
                cascadeAnimationMode = true
            ).setGeometricProperties(
                circularMenuHeight = 300,
                circularMenuWidth = 300,
            ).setExpectValues(
                expectStartAngle = -11.599998f,
                expectSweepAngle = -90.8f,
                expectLeft = 123.3f,
                expectTop = 123.3f,
                expectRight = 176.7f,
                expectBottom = 176.7f,
            ).build()
        testCalculateSectorParameters(case)
    }

    @Test
    fun testCalculateSectorParameters_returnNull() {
        val case = TestCase.CalculateSectorParametersTestCaseBuilder()
            .setSectorProperties(
                sectorIndex = 1,
                startAngle = 4533,
                endAngle = 5065,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                angleOffset = 1030f,
                outerRadiusOffset = 22000f,
                cascadeAnimationMode = true
            ).setGeometricProperties(
                circularMenuHeight = 22200,
                circularMenuWidth = 22200,
            ).setExpectValues(
                expectStartAngle = 4533f,
                expectSweepAngle = 532f,
                expectLeft = 0f,
                expectTop = 0f,
                expectRight = 22200f,
                expectBottom = 22200f,
            ).build()
        testCalculateSectorParameters(case = case, ifNull = { assertTrue(true) } )
    }

    @Test
    fun testCalcSectorIconBounds_case1() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 1,
                startAngle = 20,
                endAngle = 60,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = 22000f,
            ).setGeometricProperties(
                circularMenuHeight = 22200,
                circularMenuWidth = 22200,
            ).setExpectValues(
                expectLeft = 11651f,
                expectTop = 10967f,
                expectRight = 19051f,
                expectBottom = 18367f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcSectorIconBounds_case2() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                startAngle = 20,
                endAngle = 60,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = 22000f,
            ).setGeometricProperties(
                circularMenuHeight = 22200,
                circularMenuWidth = 22200,
            ).setExpectValues(
                expectLeft = 10558f,
                expectTop = 11230f,
                expectRight = 3291f,
                expectBottom = 3963f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcSectorIconBounds_case3() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                startAngle = 65,
                endAngle = 120,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = 20f,
            ).setGeometricProperties(
                circularMenuHeight = 100,
                circularMenuWidth = 50,
            ).setExpectValues(
                expectLeft = 48f,
                expectTop = 25f,
                expectRight = 51f,
                expectBottom = 29f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcSectorIconBounds_case4() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                startAngle = 65,
                endAngle = 120,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = 0f,
            ).setGeometricProperties(
                circularMenuHeight = 100,
                circularMenuWidth = 50,
            ).setExpectValues(
                expectLeft = 41f,
                expectTop = 29f,
                expectRight = 57f,
                expectBottom = 45f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcSectorIconBounds_case5() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                startAngle = -40,
                endAngle = 10,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = -30f,
            ).setGeometricProperties(
                circularMenuHeight = -100,
                circularMenuWidth = -50,
            ).setExpectValues(
                expectLeft = -52f,
                expectTop = -15f,
                expectRight = -66f,
                expectBottom = -29f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcSectorIconBounds_case6() {
        val case = TestCase.CalculateSectorIconBoundsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                startAngle = -40,
                endAngle = 10,
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = -34.4324f,
            ).setGeometricProperties(
                circularMenuHeight = -100,
                circularMenuWidth = -50,
            ).setExpectValues(
                expectLeft = -52f,
                expectTop = -17f,
                expectRight = -62f,
                expectBottom = -28f,
            ).build()
        testCalculateSectorIconBounds(case)
    }

    @Test
    fun testCalcCenterButtonRadius_case1() {
        val expect = 80f
        val result = geometryCalculator.calcCenterButtonRadius(
            100f,
            20f,
            0f
        )
        assertEquals(expect, result)
    }

    @Test
    fun testCalcCenterButtonRadius_case2() {
        val expect = 33.2217f
        val result = geometryCalculator.calcCenterButtonRadius(
            34.543f,
            1.3213f,
            0f
        )
        assertEquals(expect, result)
    }

    @Test
    fun testCalcCenterButtonRadius_case4() {
        val expect = -35.8643f
        val result = geometryCalculator.calcCenterButtonRadius(
            -34.543f,
            1.3213f,
            0f
        )
        assertEquals(expect, result)
    }

    @Test
    fun testCalcCenterButtonIconBounds_case1() {
        val expectLeft = -55
        val expectTop = -45
        val expectRight = 95
        val expectBottom = 105
        val (left, top, right, bottom) = geometryCalculator.calcCenterButtonIconBounds(
            100f,
            20f,
            30f
        )
        assertEquals(expectLeft, left)
        assertEquals(expectTop, top)
        assertEquals(expectRight, right)
        assertEquals(expectBottom, bottom)
    }

    @Test
    fun testCalcCenterButtonIconBounds_case2() {
        val expectLeft = -75
        val expectTop = -75
        val expectRight = 75
        val expectBottom = 75
        val (left, top, right, bottom) = geometryCalculator.calcCenterButtonIconBounds(
            100f,
            0f,
            0f
        )
        assertEquals(expectLeft, left)
        assertEquals(expectTop, top)
        assertEquals(expectRight, right)
        assertEquals(expectBottom, bottom)
    }

    @Test
    fun testCalcCenterButtonIconBounds_case3() {
        val expectLeft = 74
        val expectTop = 76
        val expectRight = -76
        val expectBottom = -74
        val (left, top, right, bottom) = geometryCalculator.calcCenterButtonIconBounds(
            -100f,
            -1f,
            1f
        )
        assertEquals(expectLeft, left)
        assertEquals(expectTop, top)
        assertEquals(expectRight, right)
        assertEquals(expectBottom, bottom)
    }

    @Test
    fun testCalcCenterButtonIconBounds_case4() {
        val expectLeft = 74
        val expectTop = 76
        val expectRight = -76
        val expectBottom = -74
        val (left, top, right, bottom) = geometryCalculator.calcCenterButtonIconBounds(
            -100f,
            -1f,
            1f
        )
        assertEquals(expectLeft, left)
        assertEquals(expectTop, top)
        assertEquals(expectRight, right)
        assertEquals(expectBottom, bottom)
    }

    @Test
    fun testCalcSectorSpacerPoints_case1() {
        val case = TestCase.CalculateSectorSpacerPointsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                angle = 20
            ).setAnimationProperties(
                animatedSectorIndex = 3,
                outerRadiusOffset = 34.4324f,
                innerRadiusOffset = 40f
            ).setGeometricProperties(
                circularMenuHeight = 200,
                circularMenuWidth = 400,
                innerRadius = 20f,
                centerButtonPadding = 5f,
                sectorSpacing = 5f
            ).setExpectValues(
                expectStartX = 76.50768f,
                expectStartY = 191.4495f,
                expectEndX = 161.61339f,
                expectEndY = 222.42545f,
                expectWidth = 2.5f
            ).build()

        testCalculateSectorSpacerPoints(case)
    }

    @Test
    fun testCalcSectorSpacerPoints_case2() {
        val case = TestCase.CalculateSectorSpacerPointsBuilder()
            .setSectorProperties(
                sectorIndex = 1,
                angle = 20
            ).setAnimationProperties(
                animatedSectorIndex = 2,
                outerRadiusOffset = 34.4324f,
                innerRadiusOffset = 40f
            ).setGeometricProperties(
                circularMenuHeight = 200,
                circularMenuWidth = 400,
                innerRadius = 20f,
                centerButtonPadding = 5f,
                sectorSpacing = 5f
            ).setExpectValues(
                expectStartX = 76.50768f,
                expectStartY = 191.4495f,
                expectEndX = 193.96927f,
                expectEndY = 234.20201f,
                expectWidth = 2.5f
            ).build()

        testCalculateSectorSpacerPoints(case)
    }

    @Test
    fun testCalcSectorSpacerPoints_case3() {
        val case = TestCase.CalculateSectorSpacerPointsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                angle = 543
            ).setAnimationProperties(
                animatedSectorIndex = 2,
                outerRadiusOffset = -34.4324f,
                innerRadiusOffset = 56.353f
            ).setGeometricProperties(
                circularMenuHeight = -100,
                circularMenuWidth = 400,
                innerRadius = 20f,
                centerButtonPadding = 4.32f,
                sectorSpacing = 0.934f
            ).setExpectValues(
                expectStartX = -9.382741f,
                expectStartY = 202.12866f,
                expectEndX = -0.068523265f,
                expectEndY = 202.61679f,
                expectWidth = 0.467f
            ).build()

        testCalculateSectorSpacerPoints(case)
    }

    @Test
    fun testCalcSectorSpacerPoints_case4() {
        val case = TestCase.CalculateSectorSpacerPointsBuilder()
            .setSectorProperties(
                sectorIndex = 3,
                angle = 40
            ).setAnimationProperties(
                animatedSectorIndex = 2,
                outerRadiusOffset = 50f,
                innerRadiusOffset = 30f
            ).setGeometricProperties(
                circularMenuHeight = 80,
                circularMenuWidth = 80,
                innerRadius = 40f,
                centerButtonPadding = 1f,
                sectorSpacing = 2f
            ).setExpectValues(
                expectStartX = 46.8944f,
                expectStartY = 45.785088f,
                expectEndX = 70.64178f,
                expectEndY = 65.7115f,
                expectWidth = 1f
            ).build()

        testCalculateSectorSpacerPoints(case)
    }

    private fun testCalculateSectorSpacerPoints(case: TestCase) = with(case) {
        val (startX, startY, endX, endY, width) = geometryCalculator.calcSectorSpacerPoints(
            sectorIndex = sectorIndex,
            animatedSectorIndex = animatedSectorIndex,
            angle = angle.toDouble(),
            outerRadius = outerRadius,
            outerRadiusOffset = outerRadiusOffset,
            innerRadius = innerRadius,
            innerRadiusOffset = innerRadiusOffset,
            centerX = centerX,
            centerY = centerY,
            centerButtonPadding = centerButtonPadding,
            sectorSpacing = sectorSpacing
        )
        assertEquals(expectStartX, startX)
        assertEquals(expectStartY, startY)
        assertEquals(expectEndX, endX)
        assertEquals(expectEndY, endY)

        assertEquals(expectWidth, width)
    }

    private fun testCalculateSectorIconBounds(case: TestCase) = with(case) {
        val (left, top, right, bottom) = geometryCalculator.calcSectorIconBounds(
            sectorIndex = sectorIndex,
            animatedSectorIndex = animatedSectorIndex,
            startAngle = startAngle.toFloat(),
            endAngle = endAngle.toFloat(),
            outerRadius = outerRadius,
            outerRadiusOffset = outerRadiusOffset,
            innerRadius = innerRadius,
            centerX = centerX,
            centerY = centerY
        )
        assertEquals(expectLeft.toInt(), left)
        assertEquals(expectTop.toInt(), top)
        assertEquals(expectRight.toInt(), right)
        assertEquals(expectBottom.toInt(), bottom)
    }

    private fun testCalculateSectorParameters(
        case: TestCase,
        ifNull: () -> Unit = { assertFalse(true) }
    ) {
        with(case) {
            geometryCalculator.calcSectorParameters(
                sectorIndex = sectorIndex,
                animatedSectorIndex = animatedSectorIndex,
                startAngle = startAngle.toFloat(),
                endAngle = endAngle.toFloat(),
                angleOffset = angleOffset,
                outerRadius = outerRadius,
                outerRadiusOffset = outerRadiusOffset,
                centerY = centerY,
                centerX = centerX,
                cascadeAnimationMode = cascadeAnimationMode,
                outerRectFLeft = outerRectFLeft,
                outerRectFTop = outerRectFTop,
                outerRectFRight = outerRectFRight,
                outerRectFBottom = outerRectFBottom,
            )?.let { param ->
                assertEquals(expectStartAngle, param.startAngle)
                assertEquals(expectSweepAngle, param.sweepAngle)

                assertEquals(expectLeft, param.left)
                assertEquals(expectTop, param.top)
                assertEquals(expectRight, param.right)
                assertEquals(expectBottom, param.bottom)
            } ?: run {
                ifNull.invoke()
            }
        }
    }

}