package com.cs4530.lifestyleapp

import com.google.gson.annotations.SerializedName

class WeatherData {
    @SerializedName("temp")
    var temperature: Int? = null
    @SerializedName("temp_max")
    var tempHigh: Int? = null
    @SerializedName("temp_min")
    var tempLow: Int? = null
    var humidity: Double? = null
}