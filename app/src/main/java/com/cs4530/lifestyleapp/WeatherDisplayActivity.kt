package com.cs4530.lifestyleapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import java.io.File

class WeatherDisplayActivity : AppCompatActivity() {

    private var locationReceived : String? = null
    private var temperatureReceived : String? = null
    private var highReceived : String? = null
    private var lowReceived : String? = null
    private var weatherReceived : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_view)

        //Get the intent that created this activity.
        val receivedIntent = intent

        //Get the string data
        locationReceived = receivedIntent.getStringExtra("LOCATION")

        // Display received data in text views
        findViewById<TextView>(R.id.location_value)!!.text = getString(R.string.weather_header, locationReceived!!.toString())
    }
}