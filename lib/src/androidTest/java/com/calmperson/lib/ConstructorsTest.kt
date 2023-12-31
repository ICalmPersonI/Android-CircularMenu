package com.calmperson.lib

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.calmperson.lib.TestUtils.obtainColors
import com.calmperson.lib.TestUtils.obtainIconsIds
import com.calmperson.lib.TestUtils.pixelsToDp
import com.calmperson.lib.circularmenu.R

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.lang.IllegalArgumentException


@RunWith(AndroidJUnit4::class)
class ConstructorsTest {

    private lateinit var instrumentationContext: Context

    @Before
    fun initMenu() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testFirstConstructor_onlyRequiredArgs() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        assertNotEquals(null, icons)
        val expectedSectorsNumber = icons!!.size
        with(
            CircularMenu(context = instrumentationContext, icons = icons)
        ) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), colors)
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(Drawer.DEFAULT_COLOR, sector.color)
                assertEquals(Drawer.DEFAULT_COLOR, sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
            assertEquals(Drawer.DEFAULT_PRESSED_BUTTON_COLOR, drawer.sectorPressedColor)
            assertEquals(Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID, drawer.centerButtonIconResId)
            assertEquals(Drawer.DEFAULT_COLOR, drawer.centerButtonColor)
            assertEquals(Drawer.DEFAULT_PRESSED_BUTTON_COLOR, drawer.centerButtonPressedColor)
            assertEquals(
                Drawer.DEFAULT_BUTTON_SPACING.pixelsToDp(instrumentationContext),
                drawer.geometricProperties.sectorSpacing
            )
            assertEquals(
                Drawer.DEFAULT_CENTER_BUTTON_PADDING.pixelsToDp(instrumentationContext),
                drawer.geometricProperties.centerButtonPadding
            )
        }
    }

    @Test
    fun testSecondConstructor_onlyRequiredArgs() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        assertNotEquals(null, icons)
        val expectedSectorsNumber = icons!!.size

        with(CircularMenu(context = instrumentationContext, iconsArrayId = R.array.icons)) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), colors)
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(Drawer.DEFAULT_COLOR, sector.color)
                assertEquals(Drawer.DEFAULT_COLOR, sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
            assertEquals(Drawer.DEFAULT_PRESSED_BUTTON_COLOR, drawer.sectorPressedColor)
            assertEquals(Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID, drawer.centerButtonIconResId)
            assertEquals(Drawer.DEFAULT_COLOR, drawer.centerButtonColor)
            assertEquals(Drawer.DEFAULT_PRESSED_BUTTON_COLOR, drawer.centerButtonPressedColor)
            assertEquals(
                Drawer.DEFAULT_BUTTON_SPACING.pixelsToDp(instrumentationContext),
                drawer.geometricProperties.sectorSpacing
            )
            assertEquals(
                Drawer.DEFAULT_CENTER_BUTTON_PADDING.pixelsToDp(instrumentationContext),
                drawer.geometricProperties.centerButtonPadding
            )
        }

    }

    @Test
    fun testFirstConstructor_allArgs() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        val colors = instrumentationContext.obtainColors(R.array.test_colors)
        val centerButtonColor = instrumentationContext.getColor(R.color.color_default)
        val pressedColor = instrumentationContext.getColor(R.color.pressed_color)
        assertNotEquals(null, icons)
        assertNotEquals(null, colors)
        val expectedSectorsNumber = icons!!.size

        with(
            CircularMenu(
                context = instrumentationContext,
                icons = icons,
                colors = colors!!,
                sectorsPressedColor = pressedColor,
                centerButtonIconId = R.drawable.cloud_snow,
                centerButtonColor = centerButtonColor,
                centerButtonPressedColor = pressedColor,
                buttonSpacingDp = 10f,
                centerButtonPaddingDp = 10f
            )
        ) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(colors, this.colors)
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(colors[i % colors.size], sector.color)
                assertEquals(colors[i % colors.size], sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
            assertEquals(pressedColor, drawer.sectorPressedColor)
            assertEquals(R.drawable.cloud_snow, drawer.centerButtonIconResId)
            assertEquals(centerButtonColor, drawer.centerButtonColor)
            assertEquals(pressedColor, drawer.centerButtonPressedColor)
            assertEquals(10f.pixelsToDp(instrumentationContext), drawer.geometricProperties.sectorSpacing)
            assertEquals(10f.pixelsToDp(instrumentationContext), drawer.geometricProperties.centerButtonPadding)
        }
    }

    @Test
    fun testSecondConstructor_allArgs() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        val colors = instrumentationContext.obtainColors(R.array.test_colors)
        assertNotEquals(null, icons)
        assertNotEquals(null, colors)
        val pressedColor = instrumentationContext.getColor(R.color.pressed_color)
        val centerButtonColor = instrumentationContext.getColor(R.color.color_default)
        val expectedSectorsNumber = icons!!.size

        with(
            CircularMenu(
                context = instrumentationContext,
                iconsArrayId = R.array.icons,
                colorsArrayId = R.array.test_colors,
                sectorsPressedColorId = R.color.pressed_color,
                centerButtonIconId = R.drawable.cloud_snow,
                centerButtonColorId = R.color.color_default,
                centerButtonPressedColorId = R.color.pressed_color,
                buttonSpacingDp = 10f,
                centerButtonPaddingDp = 10f
            )
        ) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(colors, this.colors)
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(colors!![i % colors.size], sector.color)
                assertEquals(colors[i % colors.size], sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
            assertEquals(pressedColor, drawer.sectorPressedColor)
            assertEquals(R.drawable.cloud_snow, drawer.centerButtonIconResId)
            assertEquals(centerButtonColor, drawer.centerButtonColor)
            assertEquals(pressedColor, drawer.centerButtonPressedColor)
            assertEquals(10f.pixelsToDp(instrumentationContext), drawer.geometricProperties.sectorSpacing)
            assertEquals(10f.pixelsToDp(instrumentationContext), drawer.geometricProperties.centerButtonPadding)
        }

    }

    @Test
    fun testFirstConstructor_emptyIconsArray_throwIllegalArgumentException() {
        try {
            CircularMenu(context = instrumentationContext, icons = intArrayOf())
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSecondConstructor_emptyIconsArray_throwIllegalArgumentException() {
        try {
            CircularMenu(context = instrumentationContext, iconsArrayId = R.array.empty_icons)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testFirstConstructor_moreThanTenIcons_throwIllegalArgumentException() {
        try {
            val icons = instrumentationContext.obtainIconsIds(R.array.more_than_ten_icons)
            assertNotEquals(null, icons)
            CircularMenu(context = instrumentationContext, icons = icons!!)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSecondConstructor_oneIcon_throwIllegalArgumentException() {
        try {
            CircularMenu(context = instrumentationContext, iconsArrayId = R.array.one_icon)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testFirstConstructor_oneIcon_throwIllegalArgumentException() {
        try {
            val icons = instrumentationContext.obtainIconsIds(R.array.one_icon)
            assertNotEquals(null, icons)
            CircularMenu(context = instrumentationContext, icons = icons!!)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSecondConstructor_moreThanTenIcons_throwIllegalArgumentException() {
        try {
            CircularMenu(context = instrumentationContext, iconsArrayId = R.array.more_than_ten_icons)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testFirstConstructor_emptyColorsArray() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        val colors = instrumentationContext.obtainColors(R.array.empty_colors)
        assertNotEquals(null, icons)
        assertNotEquals(null, colors)
        val expectedSectorsNumber = icons!!.size
        with(CircularMenu(context = instrumentationContext, icons = icons, colors = colors!!)) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), this.colors )
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(Drawer.DEFAULT_COLOR, sector.color)
                assertEquals(Drawer.DEFAULT_COLOR, sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
        }
    }

    @Test
    fun testSecondConstructor_emptyColorsArray() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        assertNotEquals(null, icons)
        val expectedSectorsNumber = icons!!.size
        with(
            CircularMenu(
                context = instrumentationContext,
                iconsArrayId = R.array.icons,
                colorsArrayId = R.array.empty_colors
            )
        ) {
            assertEquals(expectedSectorsNumber, sectors.size)
            assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), this.colors)
            sectors.forEachIndexed { i, sector ->
                val expectedIconId = icons[i]
                val sizeOfSector = 360 / expectedSectorsNumber
                assertEquals(sizeOfSector * i, sector.startAngle)
                assertEquals(sizeOfSector * (i + 1), sector.endAngle)
                assertEquals(expectedIconId, sector.iconId)
                assertEquals(Drawer.DEFAULT_COLOR, sector.color)
                assertEquals(Drawer.DEFAULT_COLOR, sector.currentColor)
                assertEquals(drawer.sectorPressedColor, sector.pressedColor)
            }
        }
    }

}