package com.cs4530.lifestyleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

class WeatherFragment: Fragment() {
    private var temperatureReceived : String? = null
    private var highReceived : String? = null
    private var lowReceived : String? = null
    private var humidityReceived : String? = null

    private var temperatureTile : TextView? = null

    // Get the view model
    private val mViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_view, container, false)

        mViewModel.dataWeather.observe(requireActivity(), liveDataObserver)
        temperatureTile = view.findViewById<TextView>(R.id.weather_tile)

        //Get the string data
        if (arguments != null) {
            temperatureReceived = requireArguments().getString("TEMP")
            highReceived = requireArguments().getString("TEMP_HIGH")
            lowReceived = requireArguments().getString("TEMP_LOW")
            humidityReceived = requireArguments().getString("HUMIDITY")
        }

        temperatureTile!!.text = "$temperatureReceived º"

        return view
    }

    //create an observer that watches the LiveData<WeatherData> object
    private val liveDataObserver: Observer<WeatherTable> =
        Observer { weatherData -> // Update the UI if this data variable changes
            if (weatherData != null) {
                temperatureTile!!.text = "" + weatherData.temperature + "º F"
            }
        }
}