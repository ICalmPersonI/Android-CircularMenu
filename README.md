# Circular Menu Android Library

Circular Menu is an Android library that provides a customizable circular menu component for your Android app. It allows for easy integration of circular menus with various customization options.

## Installation

To integrate Circular Menu into your Android project, add the following dependency in your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.example:circular-menu:1.0.0'
}
```
## Usage
### Constructor  
The Circular Menu constructor accepts various parameters to customize its appearance:

### Initializing and Animations

- `startOpenAnimation()`: Initiates the opening animation.
- `startCloseAnimation(removeViewAfterEnd: Boolean = false)`: Initiates the closing animation, removing the view from the parent layout upon animation completion if specified.

### Customization

- `setColors(colors: IntArray)`: Sets colors for sectors, retrieving values using a formula.
- `setIcons(iconsIds: IntArray, useChangeSectorsAnimation: Boolean)`: Sets icons for sectors, with an option to trigger sector change animation.
- `setCenterButtonIconId(@DrawableRes iconId: Int)`: Sets the icon for the central button.
- `setCenterButtonColorById(@ColorRes colorId: Int)`: Sets the color for the central button using a color resource.
- `setCenterButtonColor(color: Int)`: Sets the color for the central button using an integer value.
- `setButtonSpacing(px: Float)`: Sets the distance between sectors in dp.
- `setCenterButtonPadding(px: Float)`: Sets padding from the inner radius of the circle to the button in dp.
- `setCenterButtonPressedColorById(@ColorRes colorId: Int)`: Sets the color of the central button when pressed, using a color resource.
- `setCenterButtonPressedColor(color: Int)`: Sets the color of the central button when pressed using an integer value.

### Event Handling

- `setAnimationEventListener(listener: AnimationEventListener)`: Overrides the AnimationEventListener.
- `setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit)`: Overrides the onCenterButtonClickListener.
- `setOnSectorClickListener(listener: (CircularMenu, sectorIndex: Int) -> Unit)`: Overrides the onSectorClickListener.

### Utility Functions

- `removeThisViewFromParent()`: Removes the CircularMenu from the parent layout.
- `size(): Int`: Returns the number of sectors (buttons).
- `clearAnimation()`: Clears all animations and interrupts the chain of sequential animations.
- `setBackgroundColor(color: Int)`: Does nothing, purposefully overridden to disallow setting a background.


### Sector Properties
The Sector object has the following mutable properties that can be changed:

```var icon: Int```: Icon resource for the sector.  
```var defaultColor: Int```: Default color resource for the sector.  
```var currentColor: Int```: Current color resource for the sector.  
```var startCloseAnimationAfterClick```: Boolean: Boolean indicating if the close animation should start after a sector click.  

### Attributes
The library provides several XML attributes to customize the Circular Menu in your layout file:

```circleMenu_icons``` (format: reference): Array of icons for the menu sectors.  
```circleMenu_colors``` (format: reference): Array of colors for the menu sectors.  
```circleMenu_centerButtonIcon``` (format: reference): Icon resource for the center button.  
```circleMenu_centerButtonColor``` (format: reference): Color resource for the center button.  
```circleMenu_buttonSpacing``` (format: dimension): Spacing between menu buttons.  
```circleMenu_centerButtonPadding``` (format: dimension): Padding for the center button.    

Feel free to explore and customize the Circular Menu library to suit your app's needs.
