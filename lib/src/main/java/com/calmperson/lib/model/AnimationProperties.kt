package com.calmperson.lib.model

class AnimationProperties {

    companion object {

        private const val DEFAULT_ANIMATED_SECTOR_INDEX = 0

        private const val DEFAULT_OUTER_RADIUS_OFFSET = 0f
        private const val DEFAULT_INNER_RADIUS_OFFSET = 0f
        private const val DEFAULT_ANGLE_OFFSET = 0f

        private const val DEFAULT_CENTER_BUTTON_ROTATION = 0f

        private const val CASCADE_ANIMATION_MODE_DEFAULT_VALUE = false

        private const val COLLAPSE_SECTOR_ANIMATION_DURATION_DIVIDEND = 600L
        private const val EXPAND_SECTOR_ANIMATION_DURATION_DIVIDEND = 600L

        private const val START_CENTER_BUTTON_ROTATE_POSITION = 0f
        private const val END_CENTER_BUTTON_ROTATE_POSITION = 180f

        private const val COLLAPSE_CENTER_BUTTON_DURATION = 600L
        private const val EXPAND_CENTER_BUTTON_DURATION = 600L

        private const val PRESS_SECTOR_ANIMATION_DURATION = 100L
        private const val RELEASE_SECTOR_ANIMATION_DURATION = 100L

        private const val PRESS_CENTER_BUTTON_ANIMATION_DURATION = 100L
        private const val RELEASE_CENTER_BUTTON_ANIMATION_DURATION = 100L
    }

    /**
     * Index of the sector currently being animated.
     */
    var animatedSectorIndex: Int = DEFAULT_ANIMATED_SECTOR_INDEX
    /**
     * Offset of the outer radius of the menu for the sector currently being animated (`animatedSectorIndex`).
     */
    var outerRadiusOffset: Float = DEFAULT_OUTER_RADIUS_OFFSET
    /**
     * Offset of the inner radius of the menu, meaning the radius of the central button.
     */
    var innerRadiusOffset: Float = DEFAULT_INNER_RADIUS_OFFSET
    /**
     * Offset in the angle of the menu radius for the sector currently being animated (`animatedSectorIndex`).
     * The angle offset applies to both the starting and ending angles, creating a collapsing effect.
     */
    var angleOffset: Float = DEFAULT_ANGLE_OFFSET
    /**
     * The rotation value of the circular menu around the x-axis.
     */
    var centerButtonRotation: Float = DEFAULT_CENTER_BUTTON_ROTATION
    /**
     * Flag indicating that it's not a cascade animation for all sectors, but only for a specific sector (`animatedSectorIndex`).
     */
    var cascadeAnimationMode: Boolean = CASCADE_ANIMATION_MODE_DEFAULT_VALUE

    var startCenterButtonRotatePosition: Float = START_CENTER_BUTTON_ROTATE_POSITION
    var endCenterButtonRotatePosition: Float = END_CENTER_BUTTON_ROTATE_POSITION

    var collapseSectorAnimationDuration: Long = COLLAPSE_SECTOR_ANIMATION_DURATION_DIVIDEND
    var expandSectorAnimationDuration: Long = EXPAND_SECTOR_ANIMATION_DURATION_DIVIDEND

    var collapseCenterButtonDuration: Long = COLLAPSE_CENTER_BUTTON_DURATION
    var expandCenterButtonDuration: Long = EXPAND_CENTER_BUTTON_DURATION

    var pressSectorAnimationDuration: Long = PRESS_SECTOR_ANIMATION_DURATION
    var releaseSectorAnimationDuration: Long = RELEASE_SECTOR_ANIMATION_DURATION

    var pressCenterButtonAnimationDuration: Long = PRESS_CENTER_BUTTON_ANIMATION_DURATION
    var releaseCenterButtonAnimationDuration: Long = RELEASE_CENTER_BUTTON_ANIMATION_DURATION

    /**
     * Resets property values.
     */
    fun restoreDefaultState() {
        animatedSectorIndex = DEFAULT_ANIMATED_SECTOR_INDEX

        outerRadiusOffset = DEFAULT_OUTER_RADIUS_OFFSET
        innerRadiusOffset = DEFAULT_INNER_RADIUS_OFFSET
        angleOffset = DEFAULT_ANGLE_OFFSET

        centerButtonRotation = DEFAULT_CENTER_BUTTON_ROTATION

        cascadeAnimationMode = CASCADE_ANIMATION_MODE_DEFAULT_VALUE

        /*
        startCenterButtonRotatePosition = START_CENTER_BUTTON_ROTATE_POSITION
        endCenterButtonRotatePosition = END_CENTER_BUTTON_ROTATE_POSITION
        collapseSectorAnimationDuration = COLLAPSE_SECTOR_ANIMATION_DURATION_DIVIDEND
        expandSectorAnimationDuration = EXPAND_SECTOR_ANIMATION_DURATION_DIVIDEND
        collapseCenterButtonDuration = COLLAPSE_CENTER_BUTTON_DURATION
        expandCenterButtonDuration = EXPAND_CENTER_BUTTON_DURATION
        pressSectorAnimationDuration = PRESS_SECTOR_ANIMATION_DURATION
        releaseSectorAnimationDuration = RELEASE_SECTOR_ANIMATION_DURATION
        pressCenterButtonAnimationDuration = PRESS_CENTER_BUTTON_ANIMATION_DURATION
        releaseCenterButtonAnimationDuration = RELEASE_CENTER_BUTTON_ANIMATION_DURATION

         */
    }

}
