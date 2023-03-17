package com.cs4530.lifestyleapp

import com.google.gson.annotations.SerializedName

class WeatherData {
    @SerializedName("temp")
    var temperature: Double? = null
    @SerializedName("temp_max")
    var tempHigh: Double? = null
    @SerializedName("temp_min")
    var tempLow: Double? = null
    var humidity: Double? = null
}