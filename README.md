[![](https://jitpack.io/v/ICalmPersonI/AndroidCircularMenu.svg)](https://jitpack.io/#ICalmPersonI/AndroidCircularMenu)
# Circular Menu

## Prerequisites

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```

## Dependency

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies {
	...
	implementation 'com.calmperson.lib.circularmenu:CircularMenu:0.0.9'
}
```

## Demo
<img src="https://github-production-user-asset-6210df.s3.amazonaws.com/87424785/293511227-2e29c1cd-4baa-4e6d-a979-5b88c21addc7.gif?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20231231%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20231231T144804Z&X-Amz-Expires=300&X-Amz-Signature=a017db590991e1cf6215a6a9fd86296172f6d64fc7fdc2f0d696e6aad5f16c14&X-Amz-SignedHeaders=host&actor_id=87424785&key_id=0&repo_id=732457308" alt="1" width="280" height="420">

## Quick Start

Add an array of icons and colors (optional) to ```res/values/arrays.xml```
```xml
<resources>
    <resources>
    <array name="colors">
        <!-- Add your color items -->
    </array>
    
    <array name="icons">
        <!-- Add your icon items -->
    </array>
</resources>
</resources>
```

Include the following in your layout xml-file:
```xml
<com.calmperson.lib.CircularMenu
    android:id="@+id/circle_menu"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:clickable="true"
    android:focusable="true"
    app:circleMenu_colors="@array/colors"
    app:circleMenu_icons="@array/icons"/>
```

Alternatively, use one of the two constructors in Kotlin:
```kotlin
CircularMenu(context = this, icons = R.array.icons, colors = R.array.colors).apply {
	isClickable = true
	isFocusable = true
}
```

Or programmatically set icons and colors using::
```kotlin
CircularMenu(
    context = this,
    icons = intArrayOf(R.drawable.circle_heat, R.drawable.cloud_bolt, R.drawable.cloud_up_arrow),
    colors = intArrayOf(R.color.teal_200, R.color.teal_700, R.color.purple_500),         
).apply {
	isClickable = true
	isFocusable = true
}
```

You can specify any number of colors or none at all, but there are nuances:

- The color is taken from the color array using this formula: ```colorIndex = sectorIndex % size```
- If you don't specify any colors or pass an empty array, the default color will be set."
- These rules apply to any method of creating a Circular Menu.

Next, add a listener for the sectors.
```kotlin
setOnSectorClickListener { view, sectorIndex ->
	Log.d(TAG, "Sector index: $sectorIndex")        
}
```
For the central button, it's not mandatory to specify a listener; when clicked, the CircularMenu will be removed from the ViewGroup.

## Animations
Most animations are already implemented by default, except for the opening animation. If you need to disable the closing animation, you can simply redefine the central button press listener. Other animations are triggered only when you specify that you want to use them, based on logical flags in the functions where they are involved. If you want to use the opening animation, you'll need to define where and under what conditions to call it by yourself. All animations can be triggered independently, and there are corresponding [functions](https://github.com/ICalmPersonI/AndroidCircularMenu/blob/master/doc.md#animations) for this purpose. Before starting any animation, if the previous one hasn't finished its task yet, it will be canceled.

## Documentation
A detailed description of features and other examples can be found in the [documentation](https://github.com/ICalmPersonI/AndroidCircularMenu/blob/91be57587a37786473c7737b34bce741e83fd5c2/doc.md).
