## Constructors
```kotlin
constructor(
        context: Context?,
        @DrawableRes icons: IntArray,
        @DrawableRes centerButtonIconId: Int = Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID,
        colors: IntArray = intArrayOf(Drawer.DEFAULT_COLOR),
        centerButtonColor: Int = Drawer.DEFAULT_COLOR,
        centerButtonPressedColor: Int = Drawer.PRESSED_BUTTON_COLOR,
        buttonSpacingPx: Float = Drawer.DEFAULT_BUTTON_SPACING,
        centerButtonPaddingPx: Float = Drawer.DEFAULT_CENTER_BUTTON_PADDING
    )
```

```kotlin
constructor(
        context: Context?,
        @ArrayRes iconsArrayId: Int,
        @DrawableRes centerButtonIconId: Int = Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID,
        @ArrayRes colorsArrayId: Int,
        @ColorRes centerButtonColorId: Int,
        @ColorRes centerButtonPressedColorId: Int,
        buttonSpacingPx: Float = Drawer.DEFAULT_BUTTON_SPACING,
        centerButtonPaddingPx: Float = Drawer.DEFAULT_CENTER_BUTTON_PADDING
    )
```

## Animations
All animations are triggered by their respective method in the ```AnimationEventListener``` based on their life cycle.

- `startOpenAnimation()` Initiates the opening animation.

- `startCloseAnimation(removeViewAfterEnd: Boolean = false)` Initiates the closing animation and, upon animation completion, optionally removes the view from the parent layout.

- `startPressSectorAnimation(sectorIndex: Int)` Initiates the press animation of a specific sector (`sectorIndex`).

- `startReleaseSectorAnimation(sectorIndex: Int)` Initiates the release animation of a specific sector (`sectorIndex`).

- `startPressCenterButtonAnimation()` Initiates the press animation of the central button.

- `startReleaseCenterButtonAnimation()` Initiates the release animation of the central button.
  
- `startChangeSectorsAnimation(doWhenSectorsCollapsed: () -> Unit)` Initiates the collapse animation of all sectors, after it ended initiates the expand animation of all sectors. Using it for changing icons and colors is not recommended; for this purpose, there are `setIcons` and `setColors` functions.

- `clearAnimation()` Clears all animations, interrupting the chain of sequential animations and triggering the funtion `AnimationEventListener.on(animation_name)Cancel()`.

#### AnimationProperties class

A data class representing properties for animating the circular menu.

- `var animatedSectorIndex: Int` Index of the sector currently being animated.

- `var outerRadiusOffset: Float` Offset of the outer radius of the menu for the sector currently being animated (`animatedSectorIndex`).

- `var innerRadiusOffset: Float` Offset of the inner radius of the menu, referring to the radius of the central button.

- `var angleOffset: Float` Offset in the angle of the menu radius for the sector currently being animated (`animatedSectorIndex`). The angle offset applies to both the starting and ending angles, creating a collapsing effect.

- `var centerButtonRotation: Float` The rotation value of the circular menu around the x-axis.

- `var singleSectorAnimation: Boolean` Flag indicating that it's not a cascade animation for all sectors, but only for a specific sector (`animatedSectorIndex`).

- `restoreDefaultState()` Resets property values to their default state.


## Setters

- `setColors(colors: IntArray, useChangeSectorsAnimation: Boolean)` Sets the colors of the sectors based on the provided array. If empty, it sets a default array with a single color.

- `setIcons(iconsIds: IntArray, useChangeSectorsAnimation: Boolean)` Sets new icons for the sectors, creating new instances of the sector. Supports setting from 2 to 10 sectors.

- `setCenterButtonIconId(@DrawableRes iconId: Int)` Sets the icon for the central button.

- `setCenterButtonColorById(@ColorRes colorId: Int)` Sets the color for the central button based on the provided color resource ID.

- `setCenterButtonColor(color: Int)` Sets the color for the central button using an integer value representing the color.

- `setButtonSpacing(px: Float)` Sets the distance between sectors (buttons).

- `setCenterButtonPadding(px: Float)` Sets the padding from the inner radius of the circle to the button.

- `setCenterButtonPressedColorById(@ColorRes colorId: Int)` Sets the color of the central button when pressed, based on the provided color resource ID.

- `setCenterButtonPressedColor(color: Int)` Sets the color of the central button when pressed using an integer value representing the color.

- `setOnSectorClickListener(listener: (CircularMenu, sectorIndex: Int) -> Unit)` Overrides the `onSectorClickListener`.
```kotlin
setOnSectorClickListener { view, sectorIndex ->
        Log.d(TAG, "Sector index: $sectorIndex")
}
```

- `setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit)` Overrides the `onCenterButtonClickListener`.
```kotlin
setOnCenterButtonClickListener { view ->
        Log.d(TAG, "Center button click.")
}
```
  
- `setAnimationEventListener(listener: AnimationEventListener)` Overrides the `AnimationEventListener`.
```kotlin
setAnimationEventListener(object : AnimationEventListener {
            override fun onOpenMenuAnimationStart() {}
            override fun onOpenMenuAnimationEnd() {}
            override fun onOpenMenuAnimationCancel() {}

            override fun onCloseMenuAnimationStart() {}
            override fun onCloseMenuAnimationEnd() {}
            override fun onCloseMenuAnimationCancel() {}

            override fun onChangeSectorsAnimationStart() {}
            override fun onChangeSectorsAnimationEnd() {}
            override fun onChangeSectorsAnimationCancel() {}

            override fun onPressSectorAnimationStart() {}
            override fun onPressSectorAnimationEnd() {}
            override fun onPressSectorAnimationCancel() {}

            override fun onReleaseSectorAnimationStart() {}
            override fun onReleaseSectorAnimationEnd() {}
            override fun onReleaseSectorAnimationCancel() {}

            override fun onPressCenterButtonAnimationStart() {}
            override fun onPressCenterButtonAnimationEnd() {}
            override fun onPressCenterButtonAnimationCancel() {}

            override fun onReleaseCenterButtonAnimationStart() {}
            override fun onReleaseCenterButtonAnimationEnd() {}
            override fun onReleaseCenterButtonAnimationCancel() {}
        }
    })
```

## Other
 
- `removeThisViewFromParent()` Removes the CircularMenu from the parent layout.

- `size(): Int` Returns the number of sectors (buttons).

- `getSectorIndex(x: Float, y: Float): Int?` Returns the sector index if the point (x, y) is within one of them, returns null otherwise.

- `isPointInCenterButton(x: Float, y: Float): Boolean` Returns a boolean indicating whether the point (x, y) is within the inner radius of the circular menu (center button).

- `setBackgroundColor(color: Int)` Does nothing; overridden purposefully as setting a background should not be allowed.
