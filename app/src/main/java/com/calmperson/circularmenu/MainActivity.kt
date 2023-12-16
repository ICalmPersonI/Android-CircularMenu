package com.calmperson.circularmenu

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.calmperson.circularmenu.databinding.ActivityMainBinding
import com.calmperson.lib.CircularMenu

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {

            openButton.setOnClickListener {
                circleMenu1.startOpenAnimation()
                circleMenu2.startOpenAnimation()
            }

            circleMenu1.setAnimationEventListener(object : CircularMenu.AnimationEventListener {
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

            circleMenu1.setOnCenterButtonClickListener { circleMenu ->
                Log.i(TAG, "Center button click.")
                circleMenu.startCloseAnimation()

            }

            circleMenu1.setOnSectorClickListener { sector, sectorIndex ->
                Log.i(TAG, "Sector index: $sectorIndex")
                Log.i(TAG, sector.toString())
            }

        }
        setContentView(binding.root)
    }

}