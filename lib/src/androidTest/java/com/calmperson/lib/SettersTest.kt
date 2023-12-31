package com.calmperson.lib

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.calmperson.lib.TestUtils.obtainColors
import com.calmperson.lib.TestUtils.obtainIconsIds
import com.calmperson.lib.TestUtils.pixelsToDp
import com.calmperson.lib.circularmenu.R
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException

@RunWith(AndroidJUnit4::class)
class SettersTest {

    private lateinit var instrumentationContext: Context
    private lateinit var circularMenu: CircularMenu

    @Before
    fun initMenu() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        circularMenu = CircularMenu(
            context = instrumentationContext,
            iconsArrayId = R.array.icons
        )
    }

    @Test
    fun testSetColors_emptyColorsArrayWithoutAnimation() {
        val colors = instrumentationContext.obtainColors(R.array.empty_colors)
        assertNotEquals(null, colors)
        circularMenu.setColors(colors!!, false)
        circularMenu.sectors.forEach { sector ->
            assertEquals(intArrayOf(Drawer.DEFAULT_COLOR).first(), sector.color)
            assertEquals(intArrayOf(Drawer.DEFAULT_COLOR).first(), sector.currentColor)
        }
        assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), circularMenu.colors)
    }

    @Test
    fun testSetColors_threeColorsArrayWithoutAnimation() {
        val colors = instrumentationContext.obtainColors(R.array.test_colors)
        assertNotEquals(null, colors)
        circularMenu.setColors(colors!!, false)
        assertArrayEquals(colors, circularMenu.colors)
        circularMenu.sectors.forEachIndexed { i, sector ->
            assertEquals(colors[i % colors.size], sector.color)
            assertEquals(colors[i % colors.size], sector.currentColor)
        }
    }

    @Test
    fun testSetColorsById_emptyColorsArrayWithoutAnimation() {
        circularMenu.setColorsById(R.array.empty_colors, false)
        assertArrayEquals(intArrayOf(Drawer.DEFAULT_COLOR), circularMenu.colors)
        circularMenu.sectors.forEach { sector ->
            assertEquals(sector.color, intArrayOf(Drawer.DEFAULT_COLOR).first())
            assertEquals(sector.currentColor, intArrayOf(Drawer.DEFAULT_COLOR).first())
        }
    }

    @Test
    fun testSetColorsById_threeColorsArrayWithoutAnimation() {
        val colors = instrumentationContext.obtainColors(R.array.test_colors)
        assertNotEquals(null, colors)
        circularMenu.setColorsById(R.array.test_colors, false)
        assertArrayEquals(colors!!, circularMenu.colors)
        circularMenu.sectors.forEachIndexed { i, sector ->
            assertEquals(sector.color, colors[i % colors.size])
            assertEquals(sector.currentColor, colors[i % colors.size])
        }
    }

    @Test
    fun testSetIcons_emptyIconsArrayThrowIllegalArgumentException() {
        try {
            val icons = instrumentationContext.obtainIconsIds(R.array.empty_icons)
            assertNotEquals(null, icons)
            circularMenu.setIcons(icons!!, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSetIcons_moreThanTenIconsThrowIllegalArgumentException() {
        try {
            val icons = instrumentationContext.obtainIconsIds(R.array.more_than_ten_icons)
            assertNotEquals(null, icons)
            circularMenu.setIcons(icons!!, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSetIcons_oneIconThrowIllegalArgumentException() {
        try {
            val icons = instrumentationContext.obtainIconsIds(R.array.one_icon)
            assertNotEquals(null, icons)
            circularMenu.setIcons(icons!!, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSetIconsById_emptyIconsArrayThrowIllegalArgumentException() {
        try {
            circularMenu.setIconsById(R.array.empty_icons, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSetIconsById_moreThanTenIconsThrowIllegalArgumentException() {
        try {
            circularMenu.setIconsById(R.array.more_than_ten_icons, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun testSetIconsById_oneIconThrowIllegalArgumentException() {
        try {
            circularMenu.setIconsById(R.array.one_icon, false)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun setSetSectorPressedColor() {
        val color = instrumentationContext.getColor(R.color.purple_500)
        circularMenu.setSectorsPressedColor(color)
        assertEquals(color, circularMenu.drawer.sectorPressedColor,)
        circularMenu.sectors.forEach { sector ->
            assertEquals(color, sector.pressedColor)
        }
    }

    @Test
    fun setSetSectorPressedColorById() {
        val color = instrumentationContext.getColor(R.color.purple_500)
        circularMenu.setSectorsPressedColorById(R.color.purple_500)
        assertEquals(color, circularMenu.drawer.sectorPressedColor)
        circularMenu.sectors.forEach { sector ->
            assertEquals(color, sector.pressedColor)
        }
    }

    @Test
    fun setCenterButtonIconId() {
        circularMenu.setCenterButtonIconId(R.drawable.cloud_snow)
        assertEquals(R.drawable.cloud_snow, circularMenu.drawer.centerButtonIconResId)
    }

    @Test
    fun testSetSectorSpacing() {
        val px = 20f
        val dp = px.pixelsToDp(instrumentationContext)
        circularMenu.setSectorSpacing(px)
        assertEquals(dp, circularMenu.drawer.geometricProperties.sectorSpacing)
    }

    @Test
    fun testSetCenterButtonPadding() {
        val px = 20f
        val dp = px.pixelsToDp(instrumentationContext)
        circularMenu.setCenterButtonPadding(px)
        assertEquals(dp, circularMenu.drawer.geometricProperties.centerButtonPadding)
    }

    @Test
    fun testSetCenterButtonColorById() {
        val color = instrumentationContext.getColor(R.color.purple_700)
        circularMenu.setCenterButtonColorById(R.color.purple_700)
        assertEquals(color, circularMenu.drawer.centerButtonColor)
    }

    @Test
    fun testSetCenterButtonColor() {
        val color = instrumentationContext.getColor(R.color.purple_700)
        circularMenu.setCenterButtonColor(color)
        assertEquals(color, circularMenu.drawer.centerButtonColor)
    }

    @Test
    fun testSetCenterButtonPressedColorById() {
        val color = instrumentationContext.getColor(R.color.purple_200)
        circularMenu.setCenterButtonPressedColorById(R.color.purple_200)
        assertEquals(color, circularMenu.drawer.centerButtonPressedColor)
    }

    @Test
    fun testSetCenterButtonPressedColor() {
        val color = instrumentationContext.getColor(R.color.purple_200)
        circularMenu.setCenterButtonPressedColor(color)
        assertEquals(color, circularMenu.drawer.centerButtonPressedColor)
    }

    @Test
    fun testSetCenterButtonCurrentColorById() {
        val color = instrumentationContext.getColor(R.color.purple_200)
        circularMenu.setCenterButtonCurrentColorById(R.color.purple_200)
        assertEquals(color, circularMenu.drawer.centerButtonCurrentColor)
    }

    @Test
    fun testSetCenterButtonCurrentColor() {
        val color = instrumentationContext.getColor(R.color.purple_200)
        circularMenu.setCenterButtonColor(color)
        assertEquals(color, circularMenu.drawer.centerButtonCurrentColor)
    }

    @Test
    fun testSetOnSectorClickListener() {
        circularMenu.setOnSectorClickListener { _, _ -> assertTrue(true) }
        circularMenu.sectorPerformClick(0)
    }

    @Test
    fun testSetOnCenterButtonClickListener() {
        circularMenu.setOnCenterButtonClickListener { assertTrue(true) }
        circularMenu.centerButtonPerformClick()
    }

    @Test
    fun testSetBackgroundColor() {
        val color = instrumentationContext.getColor(R.color.purple_500)
        circularMenu.setBackgroundColor(color)
        assertEquals(null, circularMenu.background)
    }

}