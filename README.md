# Circular Menu Android Library

Circular Menu is an Android library that provides a customizable circular menu component for your Android app. It allows for easy integration of circular menus with various customization options.

## Installation

To integrate Circular Menu into your Android project, add the following dependency in your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.example:circular-menu:1.0.0'
}
```
Quick Start
Example Usage
To quickly get started with the Circular Menu library, you can initialize a basic circular menu with default settings. Below is a simple example of how you might do this:

```kotlin
Copy code
// Initialize CircularMenu
val circularMenu = CircularMenu(context)

// Customize menu attributes (optional)
// circularMenu.setCenterButtonIconId(R.drawable.icon)
// circularMenu.setColors(intArrayOf(Color.RED, Color.BLUE, Color.GREEN))

// Add CircularMenu to your layout
yourLayout.addView(circularMenu)
```
This code initializes a basic CircularMenu instance and demonstrates how to customize its attributes. For further customization options, refer to the full documentation.

Documentation
For detailed information on constructors, functions, and attributes, refer to the [full documentation here](https://github.com/ICalmPersonI/AndroidCircularMenu/blob/91be57587a37786473c7737b34bce741e83fd5c2/doc.md). This documentation covers all available methods and customization options provided by the Circular Menu library.

Feel free to explore and customize the Circular Menu library to suit your app's needs.
