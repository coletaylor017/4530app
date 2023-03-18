package com.cs4530.lifestyleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeatherFragment: Fragment() {
    private var temperatureReceived : String? = null
    private var highReceived : String? = null
    private var lowReceived : String? = null
    private var humidityReceived : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_view, container, false)

        //Get the string data
        if (arguments != null) {
            temperatureReceived = requireArguments().getString("TEMP")
            highReceived = requireArguments().getString("TEMP_HIGH")
            lowReceived = requireArguments().getString("TEMP_LOW")
            humidityReceived = requireArguments().getString("HUMIDITY")
        }

        view.findViewById<TextView>(R.id.weather_tile)!!.text = "$temperatureReceived º"

        return view
    }
}