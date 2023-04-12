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
    private var highTempTextView : TextView? = null
    private var lowTempTextView : TextView? = null
    private var humidityTextView : TextView? = null
    private var iconTextView : TextView? = null

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
        highTempTextView = view.findViewById<TextView>(R.id.high_temp_value)
        lowTempTextView = view.findViewById<TextView>(R.id.low_temp_value)
        humidityTextView = view.findViewById<TextView>(R.id.humidity_value)
        iconTextView = view.findViewById<TextView>(R.id.icon_value)

        return view
    }

    //create an observer that watches the LiveData<WeatherData> object
    private val liveDataObserver: Observer<WeatherTable> =
        Observer { weatherData -> // Update the UI if this data variable changes
            if (weatherData != null) {
                temperatureTile!!.text = "" + weatherData.temperature + "º F"
                highTempTextView!!.text = "" + weatherData.tempHigh + "º F"
                lowTempTextView!!.text = "" + weatherData.tempLow + "º F"
                humidityTextView!!.text = "" + weatherData.humidity + "%"
                iconTextView!!.text = weatherData.icon
            }
        }
}