package com.hst.osa.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider

class CameraActivity : AppCompatActivity() {
    private lateinit var rangeSlider: RangeSlider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider.values
                Log.d("onStartTrackingTouch Fr", values[0].toString())
                Log.d("onStartTrackingTouch T0", values[1].toString())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider.values
                Log.d("onStopTrackingTouch Fr", values[0].toString())
                Log.d("onStopTrackingTouch T0", values[1].toString())
            }
        })
    }

}