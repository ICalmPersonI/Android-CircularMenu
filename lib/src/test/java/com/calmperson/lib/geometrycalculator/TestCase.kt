package com.calmperson.lib.geometrycalculator

import kotlin.math.min

class TestCase {

    var sectorIndex: Int = 0
    var startAngle: Int = 0
    var endAngle: Int = 0
    var angle: Int = 0

    var centerX: Float = 0f
    var centerY: Float = 0f
    var outerRadius: Float = 0f
    var outerRectFLeft: Float = 0f
    var outerRectFTop: Float = 0f
    var outerRectFRight: Float = 0f
    var outerRectFBottom: Float = 0f
    var innerRadius: Float = 0f
    var sectorSpacing: Float = 0f
    var centerButtonPadding: Float = 0f

    var animatedSectorIndex: Int = 0
    var angleOffset: Float = 0f
    var outerRadiusOffset: Float = 0f
    var innerRadiusOffset: Float = 0f
    var cascadeAnimationMode: Boolean = false

    var expectStartAngle: Float = 0f
    var expectSweepAngle: Float = 0f
    var expectLeft: Float = 0f
    var expectTop: Float = 0f
    var expectRight: Float = 0f
    var expectBottom: Float = 0f
    var expectStartX: Float = 0f
    var expectStartY: Float = 0f
    var expectEndX: Float = 0f
    var expectEndY: Float = 0f
    var expectWidth: Float = 0f


    constructor(
        sectorIndex: Int,
        startAngle: Int,
        endAngle: Int,

        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        outerRectFLeft: Float,
        outerRectFTop: Float,
        outerRectFRight: Float,
        outerRectFBottom: Float,

        animatedSectorIndex: Int,
        angleOffset: Float,
        outerRadiusOffset: Float,
        cascadeAnimationMode: Boolean,

        expectStartAngle: Float,
        expectSweepAngle: Float,
        expectLeft: Float,
        expectTop: Float,
        expectRight: Float,
        expectBottom: Float,
    ) {
        this.sectorIndex = sectorIndex
        this.startAngle = startAngle
        this.endAngle = endAngle

        this.centerX = centerX
        this.centerY = centerY
        this.outerRadius = outerRadius
        this.outerRectFLeft = outerRectFLeft
        this.outerRectFTop = outerRectFTop
        this.outerRectFRight = outerRectFRight
        this.outerRectFBottom = outerRectFBottom
        this.innerRadius = innerRadius
        this.sectorSpacing = sectorSpacing
        this.centerButtonPadding = centerButtonPadding

        this.animatedSectorIndex = animatedSectorIndex
        this.angleOffset = angleOffset
        this.outerRadiusOffset = outerRadiusOffset
        this.cascadeAnimationMode = cascadeAnimationMode

        this.expectStartAngle = expectStartAngle
        this.expectSweepAngle = expectSweepAngle
        this.expectLeft = expectLeft
        this.expectTop = expectTop
        this.expectRight = expectRight
        this.expectBottom = expectBottom
    }

    constructor(
        sectorIndex: Int,
        startAngle: Int,
        endAngle: Int,

        centerX: Float,
        centerY: Float,
        outerRadius: Float,

        animatedSectorIndex: Int,
        outerRadiusOffset: Float,

        expectLeft: Float,
        expectTop: Float,
        expectRight: Float,
        expectBottom: Float,
    ) {
        this.sectorIndex = sectorIndex
        this.startAngle = startAngle
        this.endAngle = endAngle

        this.centerX = centerX
        this.centerY = centerY
        this.outerRadius = outerRadius

        this.animatedSectorIndex = animatedSectorIndex
        this.outerRadiusOffset = outerRadiusOffset

        this.expectLeft = expectLeft
        this.expectTop = expectTop
        this.expectRight = expectRight
        this.expectBottom = expectBottom
    }

    constructor(
        sectorIndex: Int,
        angle: Int,

        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
        centerButtonPadding: Float,
        sectorSpacing: Float,

        animatedSectorIndex: Int,
        outerRadiusOffset: Float,
        innerRadiusOffset: Float,

        expectStartX: Float,
        expectStartY: Float,
        expectEndX: Float,
        expectEndY: Float,
        expectWidth: Float,
    ) {
        this.sectorIndex = sectorIndex
        this.angle = angle
        this.centerX = centerX
        this.centerY = centerY
        this.outerRadius = outerRadius
        this.innerRadius = innerRadius
        this.centerButtonPadding = centerButtonPadding
        this.sectorSpacing = sectorSpacing
        this.animatedSectorIndex = animatedSectorIndex
        this.outerRadiusOffset = outerRadiusOffset
        this.innerRadiusOffset = innerRadiusOffset
        this.expectStartX = expectStartX
        this.expectStartY = expectStartY
        this.expectEndX = expectEndX
        this.expectEndY = expectEndY
        this.expectWidth = expectWidth
    }

    class CalculateSectorParametersTestCaseBuilder {

        private var sectorIndex: Int = 0
        private var startAngle: Int = 0
        private var endAngle: Int = 0

        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private var outerRadius: Float = 0f
        private var outerRectFLeft: Float = 0f
        private var outerRectFTop: Float = 0f
        private var outerRectFRight: Float = 0f
        private var outerRectFBottom: Float = 0f

        private var animatedSectorIndex: Int = 0
        private var angleOffset: Float = 0f
        private var outerRadiusOffset: Float = 0f
        private var cascadeAnimationMode: Boolean = false

        private var expectStartAngle: Float = 0f
        private var expectSweepAngle: Float = 0f
        private var expectLeft: Float = 0f
        private var expectTop: Float = 0f
        private var expectRight: Float = 0f
        private var expectBottom: Float = 0f

        fun setSectorProperties(
            sectorIndex: Int,
            startAngle: Int,
            endAngle: Int
        ): CalculateSectorParametersTestCaseBuilder {
            this.sectorIndex = sectorIndex
            this.startAngle = startAngle
            this.endAngle = endAngle
            return this
        }

        fun setGeometricProperties(
            circularMenuHeight: Int,
            circularMenuWidth: Int,
        ): CalculateSectorParametersTestCaseBuilder {
            this.outerRadius = min(circularMenuHeight, circularMenuWidth) / 2f
            this.centerX = circularMenuHeight / 2f
            this.centerY = circularMenuWidth / 2f
            this.outerRectFLeft = centerX - outerRadius
            this.outerRectFTop = centerY - outerRadius
            this.outerRectFRight = centerX + outerRadius
            this.outerRectFBottom = centerY + outerRadius
            return this
        }

        fun setAnimationProperties(
            animatedSectorIndex: Int,
            angleOffset: Float,
            outerRadiusOffset: Float,
            cascadeAnimationMode: Boolean
        ): CalculateSectorParametersTestCaseBuilder {
            this.animatedSectorIndex = animatedSectorIndex
            this.angleOffset = angleOffset
            this.outerRadiusOffset = outerRadiusOffset
            this.cascadeAnimationMode = cascadeAnimationMode
            return this
        }

        fun setExpectValues(
            expectStartAngle: Float,
            expectSweepAngle: Float,
            expectLeft: Float,
            expectTop: Float,
            expectRight: Float,
            expectBottom: Float,
        ): CalculateSectorParametersTestCaseBuilder {
            this.expectStartAngle = expectStartAngle
            this.expectSweepAngle = expectSweepAngle
            this.expectLeft = expectLeft
            this.expectTop = expectTop
            this.expectRight = expectRight
            this.expectBottom = expectBottom
            return this
        }

        fun build(): TestCase {
            return TestCase(
                sectorIndex,
                startAngle,
                endAngle,

                centerX,
                centerY,
                outerRadius,
                outerRectFLeft,
                outerRectFTop,
                outerRectFRight,
                outerRectFBottom,

                animatedSectorIndex,
                angleOffset,
                outerRadiusOffset,
                cascadeAnimationMode,

                expectStartAngle,
                expectSweepAngle,
                expectLeft,
                expectTop,
                expectRight,
                expectBottom,
            )
        }
    }

    class CalculateSectorSpacerPointsBuilder {
        private var sectorIndex: Int = 0
        private var angle: Int = 0

        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private var outerRadius: Float = 0f
        private var innerRadius: Float = 0f
        private var centerButtonPadding: Float = 0f
        private var sectorSpacing: Float = 0f

        private var animatedSectorIndex: Int = 0
        private var outerRadiusOffset: Float = 0f
        private var innerRadiusOffset: Float = 0f

        private var expectStartX: Float = 0f
        private var expectStartY: Float = 0f
        private var expectEndX: Float = 0f
        private var expectEndY: Float = 0f
        private var expectWidth: Float = 0f

        fun setSectorProperties(
            sectorIndex: Int,
            angle: Int,
        ): CalculateSectorSpacerPointsBuilder {
            this.sectorIndex = sectorIndex
            this.angle = angle
            return this
        }

        fun setGeometricProperties(
            circularMenuHeight: Int,
            circularMenuWidth: Int,
            innerRadius: Float,
            centerButtonPadding: Float,
            sectorSpacing: Float
        ): CalculateSectorSpacerPointsBuilder {
            this.outerRadius = min(circularMenuHeight, circularMenuWidth) / 2f
            this.centerX = circularMenuHeight / 2f
            this.centerY = circularMenuWidth / 2f
            this.innerRadius = innerRadius
            this.centerButtonPadding = centerButtonPadding
            this.sectorSpacing = sectorSpacing
            return this
        }

        fun setAnimationProperties(
            animatedSectorIndex: Int,
            outerRadiusOffset: Float,
            innerRadiusOffset: Float,
        ): CalculateSectorSpacerPointsBuilder {
            this.animatedSectorIndex = animatedSectorIndex
            this.outerRadiusOffset = outerRadiusOffset
            this.innerRadiusOffset = innerRadiusOffset
            return this
        }

        fun setExpectValues(
            expectStartX: Float,
            expectStartY: Float,
            expectEndX: Float,
            expectEndY: Float,
            expectWidth: Float,
        ): CalculateSectorSpacerPointsBuilder {
            this.expectStartX = expectStartX
            this.expectStartY = expectStartY
            this.expectEndX = expectEndX
            this.expectEndY = expectEndY
            this.expectWidth = expectWidth
            return this
        }

        fun build(): TestCase {
            return TestCase(
                sectorIndex,
                angle,

                centerX,
                centerY,
                outerRadius,
                innerRadius,
                centerButtonPadding,
                sectorSpacing,

                animatedSectorIndex,
                outerRadiusOffset,
                innerRadiusOffset,

                expectStartX,
                expectStartY,
                expectEndX,
                expectEndY,
                expectWidth,
            )
        }
    }

    class CalculateSectorIconBoundsBuilder {
        private var sectorIndex: Int = 0
        private var startAngle: Int = 0
        private var endAngle: Int = 0

        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private var outerRadius: Float = 0f

        private var animatedSectorIndex: Int = 0
        private var outerRadiusOffset: Float = 0f

        private var expectLeft: Float = 0f
        private var expectTop: Float = 0f
        private var expectRight: Float = 0f
        private var expectBottom: Float = 0f

        fun setSectorProperties(
            sectorIndex: Int,
            startAngle: Int,
            endAngle: Int
        ): CalculateSectorIconBoundsBuilder {
            this.sectorIndex = sectorIndex
            this.startAngle = startAngle
            this.endAngle = endAngle
            return this
        }

        fun setGeometricProperties(
            circularMenuHeight: Int,
            circularMenuWidth: Int,
        ): CalculateSectorIconBoundsBuilder {
            this.outerRadius = min(circularMenuHeight, circularMenuWidth) / 2f
            this.centerX = circularMenuHeight / 2f
            this.centerY = circularMenuWidth / 2f
            return this
        }

        fun setAnimationProperties(
            animatedSectorIndex: Int,
            outerRadiusOffset: Float
        ): CalculateSectorIconBoundsBuilder {
            this.animatedSectorIndex = animatedSectorIndex
            this.outerRadiusOffset = outerRadiusOffset
            return this
        }

        fun setExpectValues(
            expectLeft: Float,
            expectTop: Float,
            expectRight: Float,
            expectBottom: Float,
        ): CalculateSectorIconBoundsBuilder {
            this.expectLeft = expectLeft
            this.expectTop = expectTop
            this.expectRight = expectRight
            this.expectBottom = expectBottom
            return this
        }

        fun build(): TestCase {
            return TestCase(
                sectorIndex,
                startAngle,
                endAngle,

                centerX,
                centerY,
                outerRadius,

                animatedSectorIndex,
                outerRadiusOffset,

                expectLeft,
                expectTop,
                expectRight,
                expectBottom,
            )
        }
    }

}