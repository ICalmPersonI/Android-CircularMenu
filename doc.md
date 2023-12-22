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

- `startOpenAnimation()` Initiates the opening animation and triggers the corresponding method in the `AnimationEventListener`.

- `startCloseAnimation(removeViewAfterEnd: Boolean = false)` Initiates the closing animation and, upon animation completion, optionally removes the view from the parent layout.

- `startPressSectorAnimation(sectorIndex: Int)` Initiates the press animation of a specific sector (`sectorIndex`) and triggers the corresponding method in the `AnimationEventListener`.

- `startReleaseSectorAnimation(sectorIndex: Int)` Initiates the release animation of a specific sector (`sectorIndex`) and triggers the corresponding method in the `AnimationEventListener`.

- `startPressCenterButtonAnimation()` Initiates the press animation of the central button and triggers the corresponding method in the `AnimationEventListener`.

- `startReleaseCenterButtonAnimation()` Initiates the release animation of the central button and triggers the corresponding method in the `AnimationEventListener`.
- 
- - `clearAnimation()` Clears all animations, interrupting the chain of sequential animations and triggering the method `AnimationEventListener.on(animation_name)Cancel()`.

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

- `setAnimationEventListener(listener: AnimationEventListener)` Overrides the `AnimationEventListener`.

- `setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit)` Overrides the `onCenterButtonClickListener`.

- `setOnSectorClickListener(listener: (CircularMenu, sectorIndex: Int) -> Unit)` Overrides the `onSectorClickListener`.

## Other
 
- `removeThisViewFromParent()` Removes the CircularMenu from the parent layout.

- `size(): Int` Returns the number of sectors (buttons).

- `getSectorIndex(x: Float, y: Float): Int?` Returns the sector index if the point (x, y) is within one of them, returns null otherwise.

- `isPointInCenterButton(x: Float, y: Float): Boolean` Returns a boolean indicating whether the point (x, y) is within the inner radius of the circular menu (center button).

- `setBackgroundColor(color: Int)` Does nothing; overridden purposefully as setting a background should not be allowed.
