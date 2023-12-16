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

```kotlin
constructor(
    context: Context?,
    @DrawableRes icons: List<Int>,
    @ColorRes colors: List<Int>,
    @DrawableRes centerButtonIconId: Int,
    @ColorRes centerButtonColorId: Int,
    buttonSpacing: Float,
    centerButtonPadding: Float
)
```
Example usage:

```kotlin
val circularMenu = CircularMenu(
    context,
    listOf(R.drawable.icon1, R.drawable.icon2, R.drawable.icon3),
    listOf(R.color.color1, R.color.color2, R.color.color3),
    R.drawable.center_button_icon,
    R.color.center_button_color,
    20.0f,
    20.0f
)
```
### Methods
The Circular Menu provides several methods for customization and interaction:

```startOpenAnimation()```: Initiates the open animation of the menu.  
```startCloseAnimation()```: Initiates the close animation of the menu.  
```setColors(colors: List<Int>)```: Sets the colors for the menu sectors.  
```setCenterButtonIconId(centerButtonIconId: Int)```: Sets the icon for the center button.  
```setCenterButtonColor(centerButtonColorId: Int)```: Sets the color for the center button.  
```setButtonSpacing(buttonSpacing: Float)```: Sets the spacing between menu buttons.  
```setCenterButtonPadding(centerButtonPadding: Float)```: Sets the padding for the center button.
```setAnimationEventListener(listener: AnimationEventListener)```: Sets an event listener for menu animations.  
```kotlin
circularMenu.setAnimationEventListener(object : CircularMenu.AnimationEventListener {
                override fun onOpenMenuAnimationStart() {
                    Log.i(TAG, "onOpenMenuAnimationStart")
                }

                override fun onOpenMenuAnimationEnd() {
                    Log.e(TAG, "onOpenMenuAnimationEnd")
                }

                override fun onOpenMenuAnimationCancel() {
                    Log.e(TAG, "onOpenMenuAnimationCancel")
                }

                override fun onCloseMenuAnimationStart() {
                    Log.e(TAG, "onCloseMenuAnimationStart")
                }

                override fun onCloseMenuAnimationEnd() {
                    Log.e(TAG, "onCloseMenuAnimationEnd")
                }

                override fun onCloseMenuAnimationCancel() {
                    Log.e(TAG, "onCloseMenuAnimationCancel")
                }

            })
```
```setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit)```: Sets a click listener for the center button.  
```kotlin
circularMenu.setOnCenterButtonClickListener { circleMenu ->
                Log.i(TAG, "Center button click.")
                circleMenu.startCloseAnimation()

            }
```
```setOnSectorClickListener(listener: (Sector, sectorIndex: Int) -> Unit)```: Sets a click listener for individual sectors.  
```kotlin
circleMenu1.setOnSectorClickListener { sector, sectorIndex ->
                Log.i(TAG, "Sector index: $sectorIndex")
                Log.i(TAG, sector.toString())
            }
```

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
