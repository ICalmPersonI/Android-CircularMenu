# Circular Menu

## Prerequisites

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

## Dependency

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies {
	...
	implementation 'com.github.GrenderG:Toasty:1.5.2'
}
```

## Quick Start

Add an array of icons and colors (optional) to ```res/values/arrays.xml```
```xml
<resources>
    <array name="colors">
        <item>@color/teal_200</item>
        <item>@color/teal_700</item>
        <item>@color/purple_200</item>
        <item>@color/purple_500</item>
        <item>@color/purple_700</item>
    </array>
    
    <array name="icons">
        <item>@drawable/circle_heat</item>
        <item>@drawable/cloud_arrow_down</item>
        <item>@drawable/cloud_bolt</item>
        <item>@drawable/cloud_rainbow</item>
        <item>@drawable/cloud_snow_alt</item>
        <item>@drawable/cloud_up_arrow</item>
        <item>@drawable/droplets</item>
        <item>@drawable/hurricane_alt</item>
    </array>
</resources>
```

Add to your layout xml-file:
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

Or use this constructor:
```kotlin
CircularMenu(context = this, icons = R.array.eight_sectors)
```

If you want to set icons and colors (optional) programmatically, you can use this constructor:
```kotlin
CircularMenu(
    context = this,
    icons = intArrayOf(R.drawable.circle_heat, R.drawable.cloud_bolt, R.drawable.cloud_up_arrow),
    colors = intArrayOf(R.color.teal_200, R.color.teal_700, R.color.purple_500),         
)
```

You can specify any number of colors or none at all, but there are nuances:

- The color is taken from the color array using this formula: ```colorIndex = sectorIndex % size```
- If you don't specify any colors or pass an empty array, the default color will be set."
- These rules apply to any method of creating a Circular Menu.


A detailed description of features and other examples can be found in the [documentation](https://github.com/ICalmPersonI/AndroidCircularMenu/blob/91be57587a37786473c7737b34bce741e83fd5c2/doc.md).
