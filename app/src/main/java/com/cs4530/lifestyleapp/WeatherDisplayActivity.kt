package com.cs4530.lifestyleapp

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle

class WeatherDisplayActivity : AppCompatActivity() {

    private var locationReceived : String? = null
    private var temperatureReceived : String? = null
    private var highReceived : String? = null
    private var lowReceived : String? = null
    private var weatherReceived : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_weather_view)

        //Get the intent that created this activity.
        val receivedIntent = intent

        //Get the string data
        locationReceived = receivedIntent.getStringExtra("LOCATION")

        // Display received data in text views
        findViewById<TextView>(R.id.weather_tile)!!.text = getString(R.string.weather_header, locationReceived!!.toString())
    }
}