package com.calmperson.lib

import android.content.Context
import android.widget.FrameLayout
import androidx.core.view.size
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.calmperson.lib.TestUtils.obtainIconsIds
import com.calmperson.lib.circularmenu.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OtherFunctionsTest {

    private lateinit var instrumentationContext: Context

    @Before
    fun initMenu() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testRemoveThisViewFromParent() {
        val layout = FrameLayout(instrumentationContext)
        val circularMenu = CircularMenu(
            context = instrumentationContext,
            iconsArrayId = R.array.icons
        )
        layout.addView(circularMenu)
        assertEquals(layout.getChildAt(0), circularMenu)
        circularMenu.removeThisViewFromParent()
        assertEquals(0, layout.size)
    }

    @Test
    fun testSize() {
        val icons = instrumentationContext.obtainIconsIds(R.array.icons)
        assertNotEquals(null, icons)
        val expectSize = icons!!.size
        val circularMenu = CircularMenu(context = instrumentationContext, icons = icons)
        assertEquals(expectSize, circularMenu.size())
    }

    @Test
    fun testOnSectorClick() {
        val menu = CircularMenu(context = instrumentationContext, iconsArrayId = R.array.icons)
        menu.setOnSectorClickListener { _, sectorIndex ->
            if (sectorIndex == 0) assertTrue(true)
        }
        menu.sectorPerformClick(0)
    }

    @Test
    fun testOnCenterButtonClick() {
        val menu = CircularMenu(context = instrumentationContext, iconsArrayId = R.array.icons)
        menu.setOnCenterButtonClickListener {
            assertTrue(true)
        }
        menu.centerButtonPerformClick()
    }

}