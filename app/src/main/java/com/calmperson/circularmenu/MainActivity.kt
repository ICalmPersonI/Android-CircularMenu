package com.calmperson.circularmenu

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.calmperson.circularmenu.databinding.ActivityMainBinding
import com.calmperson.lib.CircularMenu

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var colors: IntArray
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        colors = resources.getIntArray(R.array.colors)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {

            openButton.setOnClickListener {
                gridLayout.children.forEach {
                    if (it is CircularMenu) it.startOpenAnimation()
                }
            }

            blendColorsButton.setOnClickListener {
                gridLayout.children.forEachIndexed { i, view ->
                    if (view is CircularMenu) {
                        view.setColors(colors.clone().apply { shuffle() }, false)
                    }
                }
            }

            initCircularMenuWithTwoSectors(this)
            initCircularMenuWithTwoSectors(this)

            gridLayout.addView(createCircularMenuWithFourSectorsSecondConstructor())
            gridLayout.addView(createCircularMenuWithEightSectorsAndFirstConstructor())
            gridLayout.addView(createCircularMenuWithoutArgsFirstConstructor())
            gridLayout.addView(createCircularMenuWithoutArgsSecondConstructor())
            gridLayout.addView(createMyCircularMenu())
        }


        setContentView(binding.root)
    }

    private fun initCircularMenuWithTwoSectors(binding: ActivityMainBinding) = with(binding)  {

        circleMenuTwoSectors.setOnCenterButtonClickListener { circleMenu ->
            Log.d(TAG, "Center button click.")
            //circleMenu.startCloseAnimation()
        }

        circleMenuTwoSectors.setOnSectorClickListener { view, sectorIndex ->
            Log.d(TAG, "Sector index: $sectorIndex")
            Log.d(TAG, view.size().toString())
            if (sectorIndex == view.size() - 1) {
                view.setIcons(
                    iconsIds = intArrayOf(R.drawable.circle_heat, R.drawable.cloud_bolt, R.drawable.cloud_up_arrow),
                    useChangeSectorsAnimation = true
                )
            }
        }

        circleMenuTwoSectors.setAnimationEventListener(object : CircularMenu.AnimationEventListener {
            override fun onChangeSectorsAnimationStart() {
                Log.e("Tst", "Start")
            }

            override fun onChangeSectorsAnimationEnd() {
                Log.e("Tst", "End")
            }

            override fun onChangeSectorsAnimationCancel() {
                Log.e("Tst", "cAncel")
            }
        })

        circleMenuTwoSectors.animationProperties.apply {
            collapseSectorAnimationDuration = 1000L
            expandSectorAnimationDuration = 1000L
        }

        circleMenuTwoSectors.setCenterButtonPressedColor(Color.CYAN)
    }

    private fun createCircularMenuWithoutArgsFirstConstructor(): CircularMenu {
        return CircularMenu(
            context = this,
            icons = intArrayOf(R.drawable.cance_circle, R.drawable.cloud_rainbow, R.drawable.droplets),
        ).apply {
            isClickable = true
            isFocusable = true
            setPadding(10, 10, 10, 10)
            layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
        }
    }

    private fun createCircularMenuWithoutArgsSecondConstructor(): CircularMenu {
        return CircularMenu(
            context = this,
            iconsArrayId = R.array.ten_sectors,
        ).apply {
            isClickable = true
            isFocusable = true
            setPadding(10, 10, 10, 10)
            layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
        }
    }


    private fun createCircularMenuWithEightSectorsAndFirstConstructor(): CircularMenu {
        return CircularMenu(
            context = this,
            icons = intArrayOf(R.drawable.circle_heat, R.drawable.cloud_bolt, R.drawable.cloud_up_arrow),
            centerButtonIconId = R.drawable.cance_circle,
            colors = intArrayOf(R.color.teal_200, R.color.teal_700, R.color.purple_500),
            centerButtonColor = getColor(R.color.purple_500),
            centerButtonPressedColor = getColor(R.color.white),
            buttonSpacingDp = 20f,
            centerButtonPaddingDp = 20f
        ).apply {
            isClickable = true
            isFocusable = true
            setPadding(10, 10, 10, 10)
            layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
            setBackgroundColor(context.getColor(R.color.black))
        }
    }

    private fun createCircularMenuWithFourSectorsSecondConstructor(): CircularMenu {
        return CircularMenu(
            context = this,
            iconsArrayId = R.array.four_sectors,
            centerButtonIconId = R.drawable.cance_circle,
            colorsArrayId = R.array.colors,
            centerButtonColorId = R.color.teal_200,
            centerButtonPressedColorId = R.color.teal_700,
            buttonSpacingDp = 10f,
            centerButtonPaddingDp = 10f
        ).apply {
            isClickable = true
            isFocusable = true
            setPadding(20, 20, 20, 20)
            layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
            setSectorSpacing(20f)
            setCenterButtonPadding(20f)
        }
    }

    private fun createMyCircularMenu(): MyCircularMenu {
        return MyCircularMenu(
            context = this,
            iconsArrayId = R.array.four_sectors
        ).apply {
            isClickable = true
            isFocusable = true
            setPadding(20, 20, 20, 20)
            layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
        }
    }

}